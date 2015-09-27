package com.ttn.googlehackathon.customview;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Salil Kaul on 7/7/15.
 */
public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

    static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        if (loadedImage != null) {
            ImageView imageView = (ImageView) view;
            boolean firstDisplay = !displayedImages.contains(imageUri);
            if (firstDisplay) {
                FadeInBitmapDisplayerCustom.animate(imageView, 1000);
                displayedImages.add(imageUri);
            }
        }
    }

    static class FadeInBitmapDisplayerCustom implements BitmapDisplayer {
        private int durationMillis = 0;

        private boolean animateFromNetwork = false;
        private boolean animateFromDisk = false;
        private boolean animateFromMemory = false;


        public FadeInBitmapDisplayerCustom(int durationMillis) {
            this(durationMillis, true, true, true);
        }

        /**
         * @param durationMillis     Duration of "fade-in" animation (in milliseconds)
         * @param animateFromNetwork Whether animation should be played if image is loaded from network
         * @param animateFromDisk    Whether animation should be played if image is loaded from disk cache
         * @param animateFromMemory  Whether animation should be played if image is loaded from memory cache
         */


        public FadeInBitmapDisplayerCustom(int durationMillis, boolean animateFromNetwork, boolean animateFromDisk,
                                           boolean animateFromMemory) {
            this.durationMillis = durationMillis;
            this.animateFromNetwork = animateFromNetwork;
            this.animateFromDisk = animateFromDisk;
            this.animateFromMemory = animateFromMemory;
        }

        /**
         * Animates {@link ImageView} with "fade-in" effect
         *
         * @param imageView      {@link ImageView} which display image in
         * @param durationMillis The length of the animation in milliseconds
         */
        public static void animate(View imageView, int durationMillis) {
            if (imageView != null) {
                AlphaAnimation fadeImage = new AlphaAnimation(0, 1);
                fadeImage.setDuration(durationMillis);
                fadeImage.setInterpolator(new AccelerateInterpolator());
                imageView.startAnimation(fadeImage);
            }
        }

        @Override
        public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
            imageAware.setImageBitmap(bitmap);

            if ((animateFromNetwork && loadedFrom == LoadedFrom.NETWORK) ||
                    (animateFromDisk && loadedFrom == LoadedFrom.DISC_CACHE) ||
                    (animateFromMemory && loadedFrom == LoadedFrom.MEMORY_CACHE)) {
                animate(imageAware.getWrappedView(), durationMillis);
            }
        }
    }
}