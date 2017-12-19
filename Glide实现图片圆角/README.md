glide版本为4.0以下,本文用的3.7.0

~~~~java
 //glide实现圆角
        Glide.with(this).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_tuike_dingdan) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                iv_tuike_dingdan.setImageDrawable(circularBitmapDrawable);
            }
        });
~~~~

