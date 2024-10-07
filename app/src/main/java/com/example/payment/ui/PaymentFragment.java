package com.example.payment.ui;

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
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import androidx.camera.core.ImageProxy;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class PaymentFragment extends Fragment {

    private static final String TAG = "PaymentFragment";
    private Context context;
    private TabLayout tabLayout;
    private PreviewView previewView;  // For displaying camera feed
    private static final int MY_CAMERA_REQUEST_CODE = 100;  // Define camera request code

    public PaymentFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        getViews(view);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    Toast.makeText(context, "UPI ID feature", Toast.LENGTH_SHORT).show();
                } else {
                    // Check camera permission
                    if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Request camera permission
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    } else {
                        // If permission granted, start camera
                        startCamera();
                    }

                    Toast.makeText(context, "QR Scanner feature", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private void getViews(View view) {
        tabLayout = view.findViewById(R.id.pay_tab);
        tabLayout.addTab(tabLayout.newTab().setText("UPI ID"));
        tabLayout.addTab(tabLayout.newTab().setText("SCAN QR"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initialize the preview view for the camera
        previewView = view.findViewById(R.id.previewView);
    }

    private void startCamera() {
        // Initialize CameraX
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get(); // Get the camera provider here
                bindPreview(cameraProvider); // Pass the camera provider to bindPreview
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Camera initialization error: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(context));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        // Create the preview object
        Preview preview = new Preview.Builder().build();

        // Select the back camera as default
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Set the surface provider for the PreviewView
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Create an ImageAnalysis use case
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        // Set an analyzer to the ImageAnalysis
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), this::detectQRCode); // Pass the image directly

        // Unbind use cases before rebinding
        cameraProvider.unbindAll();

        // Bind the preview and analysis to the lifecycle
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void detectQRCode(ImageProxy imageProxy) {
        InputImage inputImage;
        try {
            // Create an InputImage object from the camera frame
            inputImage = InputImage.fromMediaImage(Objects.requireNonNull(imageProxy.getImage()), imageProxy.getImageInfo().getRotationDegrees());

            // Get the barcode scanner instance
            BarcodeScanner barcodeScanner = BarcodeScanning.getClient();

            // Process the input image
            barcodeScanner.process(inputImage)
                    .addOnSuccessListener(barcodes -> {
                        // Task completed successfully
                        for (Barcode barcode : barcodes) {
                            // Get the value from the barcode
                            String value = barcode.getDisplayValue();
                            Toast.makeText(context, "QR Code: " + value, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "QR code detection failed: " + e.getMessage());
                    });
        } finally {
            imageProxy.close(); // Always close the imageProxy after processing
        }
    }
}
