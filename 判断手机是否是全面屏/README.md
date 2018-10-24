# 判断手机是否是全面屏

~~~~java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowInsets windowInsets = getWindow().getDecorView().getRootWindowInsets();
            if (windowInsets != null) {
                DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                if (displayCutout != null) {
                    List<Rect> rects = displayCutout.getBoundingRects();
                    //通过判断是否存在rects来确定是否刘海屏手机
                    if (rects != null && rects.size() > 0) {
                        isNotchScreen = true;
                    }
                }
            }
        }
~~~~

