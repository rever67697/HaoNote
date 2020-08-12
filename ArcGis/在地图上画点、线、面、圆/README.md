[TOC]

## 10.x版本画点线面圆

~~~~java
//经纬度转换为点
Point point = new Point(lon, lat));
Point gisPoint = (Point) GeometryEngine.project(point, SpatialReference.create(4326), SpatialReference.create(102113));

//画点
//Color.GREEN为画的小绿点
SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(Color.GREEN, 10, SimpleMarkerSymbol.STYLE.DIAMOND);
Graphic graphic = new Graphic(gisPoint, pointSymbol, map);
mGraphicsLayer.addGraphic(graphic);

//画图
CompositeSymbol shipSymbol = new CompositeSymbol();
Drawable drawableYellow = zoomDrawable(getResources().getDrawable(R.drawable.a0), 60, 60);
PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(drawableYellow);
shipSymbol.add(markerSymbol);
Graphic graphic = new Graphic(gisPoint, shipSymbol);
mGraphicsLayer.addGraphic(graphic);

//画线或者画面（画面只需要最后连接起点就行）
//创建多边形对象
Polygon poly = new Polygon();
//添加初始点
poly.startPath((Point) GeometryEngine.project(new Point(122.037, 25.66355), SpatialReference.create(4326), SpatialReference.create(102113)));
poly.lineTo((Point) GeometryEngine.project(new Point(122.5054766667, 25.9266466667), SpatialReference.create(4326), SpatialReference.create(102113)));
poly.lineTo((Point) GeometryEngine.project(new Point(121.751366667, 25.1649166667), SpatialReference.create(4326), SpatialReference.create(102113)));
//多边形是闭合的因此最后还要添加初始点的位置
poly.lineTo((Point) GeometryEngine.project(new Point(122.037, 25.66355), SpatialReference.create(4326), SpatialReference.create(102113)));
SimpleFillSymbol sfs = new SimpleFillSymbol(Color.RED);
sfs.setAlpha(50);
Graphic graphic = new Graphic(poly, sfs);
mGraphicsLayer.addGraphic(graphic);

//画圆
drawCircle(gisPoint, 10000, 50, Color.RED);

 /**
     * 绘制圆,配合 clearDrawCircleLayer()清除
     *
     * @param center    圆心
     * @param radius    半径
     * @param alpha     填充的透明度 0-100
     * @param fillColor 填充的颜色
     */
    public void drawCircle(Point center, double radius, int alpha, int fillColor) {
        Polygon polygon = new Polygon();
        getCircle(center, radius, polygon);
        FillSymbol symbol = new SimpleFillSymbol(fillColor);
        symbol.setAlpha(alpha);

        Graphic g = new Graphic(polygon, symbol);
        LocationLayer.addGraphic(g);
    }

 /**
     * 获取圆的图形对象
     *
     * @param center
     * @param radius
     * @return
     */
    public Polygon getCircle(Point center, double radius) {
        Polygon polygon = new Polygon();
        getCircle(center, radius, polygon);
        return polygon;
    }

    private void getCircle(Point center, double radius, Polygon circle) {
        circle.setEmpty();
        Point[] points = getPoints(center, radius);
        circle.startPath(points[0]);
        for (int i = 1; i < points.length; i++)
            circle.lineTo(points[i]);
    }

    private Point[] getPoints(Point center, double radius) {
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }
~~~~

~~~~java
	//缩放drawable
    private Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }
~~~~

## 100.x版本画圆

~~~~java
private void getCircle(Point point, double radius) {
        Point[] points = getPoints(point, radius);
        mPointCollection.clear();
        for (Point p : points) {
            mPointCollection.add(p);
        }

        Polygon polygon = new Polygon(mPointCollection);

        SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 10);
        Graphic pointGraphic = new Graphic(point, simpleMarkerSymbol);
        mGraphicsOverlay.getGraphics().add(pointGraphic);

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.parseColor("#FC8145"), 3.0f);
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.parseColor("#33e97676"), lineSymbol);

        Graphic graphic = new Graphic(polygon, simpleFillSymbol);

        mGraphicsOverlay.getGraphics().add(graphic);
    }

    /**
     * 通过中心点和半径计算得出圆形的边线点集合
     *
     * @param center
     * @param radius
     * @return
     */
    private static Point[] getPoints(Point center, double radius) {
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }

~~~~



