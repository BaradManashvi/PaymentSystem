package com.example.payment.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.payment.R;
import com.google.android.material.tabs.TabLayout;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import androidx.camera.core.ImageProxy;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class PaymentFragment extends Fragment {
    private static final String TAG = "PaymentFragment";
    private Context context;
    private TabLayout tabLayout;
    private PreviewView previewView;
    private View focusOverlay; // Reference to the overlay
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private boolean isDialogShown = false; // Flag to track dialog state

    public PaymentFragment() {
    }

    public static PaymentFragment newInstance() {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        getViews(view);

        // Inflate the QR code overlay layout
        LayoutInflater inflaterOverlay = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View overlay = inflaterOverlay.inflate(R.layout.qr_code_overlay, container, false);
        ((ViewGroup) previewView.getParent()).addView(overlay); // Add overlay to parent of previewView

        focusOverlay = overlay.findViewById(R.id.focusOverlay); // Get reference to the overlay
        focusOverlay.setVisibility(View.GONE); // Initially hide the overlay

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    Toast.makeText(context, "UPI ID feature", Toast.LENGTH_SHORT).show();
                } else {
                    if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    } else {
                        startCamera();
                    }
                    Toast.makeText(context, "QR Scanner feature", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }

    private void getViews(View view) {
        tabLayout = view.findViewById(R.id.pay_tab);
        tabLayout.addTab(tabLayout.newTab().setText("UPI ID").setContentDescription("UPI ID tab"));
        tabLayout.addTab(tabLayout.newTab().setText("SCAN QR").setContentDescription("Scan QR tab"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        previewView = view.findViewById(R.id.previewView);
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Camera initialization error: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(context));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), this::detectQRCode);
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void detectQRCode(ImageProxy imageProxy) {
        if (imageProxy.getImage() == null) {
            Log.e(TAG, "ImageProxy returned null image");
            imageProxy.close(); // Ensure the ImageProxy is closed in case of an error
            return;
        }

        InputImage inputImage = InputImage.fromMediaImage(
                Objects.requireNonNull(imageProxy.getImage()),
                imageProxy.getImageInfo().getRotationDegrees());

        BarcodeScanner barcodeScanner = BarcodeScanning.getClient();

        // Process the image asynchronously
        barcodeScanner.process(inputImage)
                .addOnSuccessListener(barcodes -> {
                    if (barcodes.isEmpty()) {
                        Log.d(TAG, "No QR code detected");
                        focusOverlay.setVisibility(View.GONE); // Hide if no QR code
                    } else {
                        for (Barcode barcode : barcodes) {
                            String value = barcode.getDisplayValue();
                            Toast.makeText(context, "QR Code: " + value, Toast.LENGTH_SHORT).show();
                            animateFocusOverlay();

                            // Check if the dialog has already been shown
                            if (!isDialogShown) {
                                isDialogShown = true; // Set the flag to true
                                showQRCodeDialog(value); // Show dialog instead of redirecting directly
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "QR code detection failed: " + e.getMessage());
                })
                .addOnCompleteListener(task -> {
                    // Ensure the ImageProxy is closed after processing is complete
                    imageProxy.close();
                });
    }

    private void showQRCodeDialog(String qrCodeData) {
        new AlertDialog.Builder(context)
                .setTitle("QR Code Detected")
                .setMessage("QR Code: " + qrCodeData)
                .setPositiveButton("Proceed", (dialog, which) -> {
                    // Do nothing here, or handle what you want after proceeding
                    isDialogShown = false; // Reset the flag when proceeding
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    isDialogShown = false; // Reset the flag if canceled
                })
                .setOnDismissListener(dialog -> isDialogShown = false) // Reset the flag when dialog is dismissed
                .show();
    }

    private void animateFocusOverlay() {
        focusOverlay.setVisibility(View.VISIBLE); // Make overlay visible when animating
        focusOverlay.setScaleX(1f);
        focusOverlay.setScaleY(1f);

        // Scale animation
        focusOverlay.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(200)
                .withEndAction(() -> {
                    focusOverlay.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(200)
                            .withEndAction(() -> focusOverlay.setVisibility(View.GONE)) // Hide after animation
                            .start();
                })
                .start();
    }
}