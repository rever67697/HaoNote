## 6.0以上检查权限

[TOC]

~~~~java
	private static final int PERMISSIONS_REQUEST_CAMERA = 454;
    private Context mContext;
    static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

	 /**
     * 检查权限
     */
    void checkSelfPermission() {
      	//PackageManager.PERMISSION_GRANTED为固定的
        if (ContextCompat.checkSelfPermission(mContext, PERMISSION_CAMERA) != 	  PackageManager.PERMISSION_GRANTED) {
            //未申请到权限,开始申请权限
            ActivityCompat.requestPermissions(this,
                    new String[]{PERMISSION_CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);
        } else {
          	//已申请到相机权限
            startWallpaper();
        }
    }

	//动态获取权限结果
 	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					//已经申请到相机权限
                    startWallpaper();

                } else {
                  	//未申请到相机权限
                    Toast.makeText(mContext, getString(R.string._lease_open_permissions), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
~~~~

