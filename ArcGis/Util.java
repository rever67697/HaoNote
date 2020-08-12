package cn.com.ahsoft.gisclient.utils;

import cn.com.ahsoft.gisclient.MainEntry;
import cn.com.ahsoft.gisclient.common.CloseWindowBase;
import cn.com.ahsoft.gisclient.common.GraphicsOverlayData;
import cn.com.ahsoft.gisclient.common.StageManager;
import cn.com.ahsoft.gisclient.compoment.MapViewMouseEvent;
import cn.com.ahsoft.gisclient.ctrl.AhMapController;
import cn.com.ahsoft.gisclient.common.ParameterInit;
import cn.com.ahsoft.gisclient.model.TreeModel;

import cn.com.ahsoft.webgis.util.StringEx;
import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.internal.jni.CoreGeometryEngine;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.AnimationCurve;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.*;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util {
    public static final SpatialReference WGS84 = SpatialReferences.getWgs84();

    public static Initializable showWin(Class clazz, String title, String fxml, String type) {
        return showWin(title, fxml, type, false, null);
    }

    public static Initializable showWin(String title, String fxml, String type) {
        return showWin(title, fxml, type, false, null);
    }

    public static Initializable showWin(String title, String fxml, String type, boolean bolTop) {
        return showWin(title, fxml, type, bolTop, null);
    }

    public static Initializable showWin(String title, String fxml, String type, Object param) {
        return showWin(title, fxml, type, false, param);
    }

    /***
     * 显示一个弹出框（fxml）
     * @param title
     * @param fxml
     */
    public static Initializable showWin(String title, String fxml, String type, boolean bolTop, Object param) {
        return showWin(title, fxml, type, bolTop, param, null, false);
    }

    public static Initializable showWin(String title, String fxml, String type, boolean bolTop, Object param, Point point, boolean bolWidth) {
        return showWin(title, fxml, type, bolTop, param, point, bolWidth, true);
    }

    public static Initializable showWin(String title, String fxml, String type, boolean bolTop, Object param, Point point, boolean bolWidth, boolean bolShow) {
        try {
            FXMLLoader loader = null;
            if (StageManager.STAGE.get(type) == null) {
                loader = new FXMLLoader(Util.class.getResource(fxml));
                Parent target = loader.load();

                Object controller = loader.getController();
                if (controller instanceof ParameterInit) {
                    ((ParameterInit) (controller)).initParam(param);
                }
                Scene scene = new Scene(target); //创建场景
                scene.getStylesheets().add("/fxml/css/modena.css");
                Stage stage = new Stage();//创建舞台
                stage.setScene(scene); //将场景载入舞台
                stage.setTitle(title);
                //stage.set
                stage.initStyle(StageStyle.UTILITY);
                if (bolTop) {
                    stage.initModality(Modality.APPLICATION_MODAL);
                }
                if (point != null) {
                    stage.setX(point.getX());
                    stage.setY(point.getY());
                }

                if (bolShow) {
                    stage.initOwner(MainEntry.getStage());
                    stage.show(); //显示窗口
                    if (point != null) {
                        stage.setX(bolWidth ? point.getX() - stage.getWidth() : point.getX());
                    }
                }

                StageManager.STAGE.put(type, stage);
                StageManager.CONTROLLER.put(type, (Initializable) controller);
                stage.setOnHidden(e -> {
                    removeWin(type);
                });

                return (Initializable) loader.getController();
            } else {
                // StageManager.STAGE.get(title).setAlwaysOnTop(true);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 移除窗口
     *
     * @param type
     */
    public static void removeWin(String type) {
        Initializable initializable = StageManager.CONTROLLER.get(type);
        if (initializable instanceof CloseWindowBase) {
            ((CloseWindowBase) initializable).closeWin();
        }

        if (StageManager.STAGE.get(type) != null) {
            StageManager.STAGE.remove(type);
        }
        if (StageManager.CONTROLLER.get(type) != null) {
            StageManager.CONTROLLER.remove(type);
        }
    }

    /**
     * 地图屏幕位置转经纬度
     *
     * @param x
     * @param y
     * @return
     */
    public static Point screenTowgs84(double x, double y) {
        Point point = new Point(x, y);
        Point mkt = (Point) GeometryEngine.project(point, SpatialReferences.getWebMercator());
        Point wgsPoint = (Point) GeometryEngine.project(mkt, WGS84);
        return wgsPoint;
    }

    /***
     * 讲经纬度转换为屏幕上的点
     * @param lon
     * @param lat
     * @return
     */
    public static Point wgs84Toscreen(double lon, double lat) {
        Point point = new Point(lon, lat);
        Point wgsPoint = (Point) GeometryEngine.project(point, WGS84);
        Point mkt = (Point) GeometryEngine.project(wgsPoint, SpatialReferences.getWebMercator());
        return mkt;
    }

    /**
     * 将double类型的数据转换为保留三位小数的String
     *
     * @param d
     * @return
     */
    public static String toStringFormat(double d) {
        return toStringFormat(d, 3);
    }

    /**
     * 将double类型的数据转换为保留三位小数的String
     *
     * @param d
     * @param pointNum 保留小数点位数
     * @return
     */
    public static String toStringFormat(double d, int pointNum) {
        BigDecimal bg = new BigDecimal(d).setScale(pointNum, BigDecimal.ROUND_HALF_UP);
        double num = bg.doubleValue();
        if (Math.round(num) - num == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }

    /***
     *  日期转换
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        if (date == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 获取图层
     * @param layerName
     * @return
     */
    public static GraphicsOverlay getGraphicsOverlay(String layerName) {
        return getGraphicsOverlay(layerName, true);
    }

    /***
     * 获取图层
     * @param layerName
     * @return
     */
    public static GraphicsOverlay getGraphicsOverlay(String layerName, boolean flag) {
        return getGraphicsOverlay(layerName, flag, true);
    }

    public static GraphicsOverlay getGraphicsOverlay(String layerName, boolean flag, boolean typeFlag) {
        GraphicsOverlay layer = GraphicsOverlayData.getOverlayMap().get(layerName);
        if (layer == null) {
            layer = new GraphicsOverlay(typeFlag ? GraphicsOverlay.RenderingMode.DYNAMIC : GraphicsOverlay.RenderingMode.STATIC);
            layer.setSelectionColor(0xFFFF0000);
            layer.setVisible(flag);
            GraphicsOverlayData.getOverlayMap().put(layerName, layer);
            if (flag) {
                GraphicsOverlayData.getMapView().getGraphicsOverlays().add(layer);
            }
        }
        return layer;
    }

    /***
     * 隐藏图形时将图形从地图上移除
     * @param layer
     * @param flag
     */
    public static void setGraphicsOverlayVis(GraphicsOverlay layer, boolean flag) {
        layer.setVisible(flag);
        if (flag && !GraphicsOverlayData.getMapView().getGraphicsOverlays().contains(layer)) {
            GraphicsOverlayData.getMapView().getGraphicsOverlays().add(layer);
        } else if (!flag && GraphicsOverlayData.getMapView().getGraphicsOverlays().contains(layer)) {
            GraphicsOverlayData.getMapView().getGraphicsOverlays().remove(layer);
        }
    }

    /***
     * 隐藏图形时将图形从地图上移除
     * @param layerName
     * @param flag
     */
    public static void setGraphicsOverlayVis(String layerName, boolean flag) {
        setGraphicsOverlayVis(getGraphicsOverlay(layerName, false), flag);
    }

    /***
     * 计算线距离
     * @param line
     * @return
     */
    public static double getLengthGeodetic(Polyline line) {
        return GeometryEngine.lengthGeodetic(line, new LinearUnit(LinearUnitId.METERS), GeodeticCurveType.GEODESIC);
    }

    /***
     * 计算线距离
     * @param line
     * @param unidId
     * @return
     */
    public static double getLengthGeodetic(Polyline line, LinearUnitId unidId) {
        return GeometryEngine.lengthGeodetic(line, new LinearUnit(unidId), GeodeticCurveType.GEODESIC);
    }

    /***
     * 计算两点之间的距离
     * @param startP
     * @param endP
     * @return
     */
    public static double getLengthGeodetic(Point startP, Point endP) {
        Polyline line = new Polyline(new PointCollection(Arrays.asList(startP, endP)));
        return GeometryEngine.lengthGeodetic(line, new LinearUnit(LinearUnitId.METERS), GeodeticCurveType.GEODESIC);
    }

    /***
     * 计算两点之间的距离
     * @param startP
     * @param endP
     * @param unit
     * @return
     */
    public static double getLengthGeodetic(Point startP, Point endP, LinearUnit unit) {
        Polyline line = new Polyline(new PointCollection(Arrays.asList(startP, endP)));
        return GeometryEngine.lengthGeodetic(line, unit, GeodeticCurveType.GEODESIC);
    }

    /**
     * 计算面积
     *
     * @param polygon
     * @return
     */
    public static double getAreaGeodetic(Polygon polygon) {
        return getAreaGeodetic(polygon, AreaUnitId.SQUARE_METERS);
    }

    /***
     * 计算面积
     * @param polygon
     * @param unitId
     * @return
     */
    public static double getAreaGeodetic(Polygon polygon, AreaUnitId unitId) {
        return Math.abs(GeometryEngine.areaGeodetic(polygon, new AreaUnit(unitId), GeodeticCurveType.GEODESIC));
    }

    /***
     * 点移动后的位置
     * @param pCenter 中心点
     * @param len 移动长度
     * @param angle 移动距离
     * @return
     */
    public static Point getMovePoint(Point pCenter, double len, double angle) {
        return GeometryEngine.moveGeodetic(pCenter, len, new LinearUnit(LinearUnitId.METERS), angle, new AngularUnit(AngularUnitId.DEGREES), GeodeticCurveType.GEODESIC);
    }

    /***
     * 在地图上画点
     * @param drawGraphicOverlay
     * @param point
     */
    public static Graphic drawPoint(GraphicsOverlay drawGraphicOverlay, Point point) {
        return drawPoint(drawGraphicOverlay, point, null);
    }

    /***
     * 在地图上画点
     * @param drawGraphicOverlay
     * @param point
     * @param attr
     * @return
     */
    public static Graphic drawPoint(GraphicsOverlay drawGraphicOverlay, Point point, Map<String, Object> attr) {
        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFFFF0000, 8);
        return drawPoint(drawGraphicOverlay, point, attr, symbol);
    }

    /***
     * 在地图上画点
     * @param drawGraphicOverlay
     * @param point
     * @param attr
     * @param symbol
     * @return
     */
    public static Graphic drawPoint(GraphicsOverlay drawGraphicOverlay, Point point, Map<String, Object> attr, MarkerSymbol symbol) {
        Graphic graphic;
        if (attr == null) {
            graphic = new Graphic(point, symbol);
        } else {
            graphic = new Graphic(point, attr, symbol);
        }
        drawGraphicOverlay.getGraphics().add(graphic);
        return graphic;
    }

    /***
     * 在地图上画线
     * @param drawGraphicOverlay
     * @param points
     */
    public static Graphic drawLine(GraphicsOverlay drawGraphicOverlay, List<Point> points) {
        return drawLine(drawGraphicOverlay, points, null);
    }

    /***
     * 在地图上画线
     * @param drawGraphicOverlay
     * @param points
     * @param attr
     * @return
     */
    public static Graphic drawLine(GraphicsOverlay drawGraphicOverlay, List<Point> points, Map<String, Object> attr) {
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFF000000, 2);
        return drawLine(drawGraphicOverlay, points, attr, lineSymbol);
    }

    /***
     * 在地图上画线
     * @param drawGraphicOverlay
     * @param points
     * @param attr
     * @param lineSymbol
     * @return
     */
    public static Graphic drawLine(GraphicsOverlay drawGraphicOverlay, List<Point> points, Map<String, Object> attr, SimpleLineSymbol lineSymbol) {
        Graphic lineGraphic = null;
        if (points.size() >= 1) {
            Polyline lineGeometry = new Polyline(new PointCollection(points));
            if (attr == null) {
                lineGraphic = new Graphic(lineGeometry, lineSymbol);
            } else {
                lineGraphic = new Graphic(lineGeometry, attr, lineSymbol);
            }
            drawGraphicOverlay.getGraphics().add(lineGraphic);
        }
        return lineGraphic;
    }

    /***
     * 在地图上画面
     * @param drawGraphicOverlay
     * @param points
     */
    public static Graphic drawArea(GraphicsOverlay drawGraphicOverlay, List<Point> points) {
        return drawArea(drawGraphicOverlay, points, null);
    }

    /***
     * 在地图上画面
     * @param drawGraphicOverlay
     * @param points
     * @param attr
     * @return
     */
    public static Graphic drawArea(GraphicsOverlay drawGraphicOverlay, List<Point> points, Map<String, Object> attr) {
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(
                SimpleLineSymbol.Style.SOLID, 0xFF000000, 1);
        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.VERTICAL, 0x00000000, lineSymbol);
        return drawArea(drawGraphicOverlay, points, attr, fillSymbol);
    }

    /***
     * 在地图上画面
     * @param drawGraphicOverlay
     * @param points
     * @param attr
     * @param fillSymbol
     * @return
     */
    public static Graphic drawArea(GraphicsOverlay drawGraphicOverlay, List<Point> points, Map<String, Object> attr, FillSymbol fillSymbol) {
        Graphic areaGraphic = null;
        if (points.size() >= 1) {
            Polygon areaGeometry = new Polygon(new PointCollection(points));
            if (attr == null) {
                areaGraphic = new Graphic(areaGeometry, fillSymbol);
            } else {
                areaGraphic = new Graphic(areaGeometry, attr, fillSymbol);
            }
            drawGraphicOverlay.getGraphics().add(areaGraphic);
        }
        return areaGraphic;
    }

    /***
     * 在地图上写字
     * @param drawGraphicOverlay
     * @param p
     * @param strTxt
     */
    public static Graphic drawText(GraphicsOverlay drawGraphicOverlay, Point p, String strTxt) {
        return drawText(drawGraphicOverlay, p, strTxt, null);
    }

    /***
     * 在地图上写字
     * @param drawGraphicOverlay
     * @param p
     * @param strTxt
     */
    public static Graphic drawText(GraphicsOverlay drawGraphicOverlay, Point p, String strTxt, Map<String, Object> attr) {
        TextSymbol symbol = new TextSymbol(10, strTxt, 0xFF000000, TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.TOP);
        symbol.setFontFamily("宋体");
        return drawText(drawGraphicOverlay, p, attr, symbol);
    }

    /***
     * 在地图上写字
     * @param drawGraphicOverlay
     * @param p
     * @param attr
     * @param symbol
     */
    public static Graphic drawText(GraphicsOverlay drawGraphicOverlay, Point p, Map<String, Object> attr, TextSymbol symbol) {
        Graphic graphic = null;
        if (attr == null) {
            graphic = new Graphic(p, symbol);
        } else {
            graphic = new Graphic(p, attr, symbol);
        }
        drawGraphicOverlay.getGraphics().add(graphic);
        return graphic;
    }

    /***
     * 画矩形
     * @param drawGraphicOverlay
     * @param points
     * @param attr
     * @param fillSymbol
     */
    public static Graphic drawRectangle(GraphicsOverlay drawGraphicOverlay, List<Point> points, Map<String, Object> attr, SimpleFillSymbol fillSymbol) {
        Graphic areaGraphic = null;
        if (points.size() == 2) {
            List<Point> list = new ArrayList<Point>();
            Point pointStart = points.get(0);
            Point pointEnd = points.get(1);

            list.add(pointStart);
            list.add(new Point(pointEnd.getX(), pointStart.getY()));
            list.add(pointEnd);
            list.add(new Point(pointStart.getX(), pointEnd.getY()));

            Polygon areaGeometry = new Polygon(new PointCollection(list));
            if (attr == null) {
                areaGraphic = new Graphic(areaGeometry, fillSymbol);
            } else {
                areaGraphic = new Graphic(areaGeometry, attr, fillSymbol);
            }
            drawGraphicOverlay.getGraphics().add(areaGraphic);
        }
        return areaGraphic;
    }

    /***
     * 显示message
     * @param title
     * @param description
     * @param type
     */
    public static void showMessage(String title, String description, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(description);
        alert.show();
    }

    /**
     * 成功提示框
     *
     * @param message
     */
    public static void showSuccess(String message) {
        showMessage("Message", message, Alert.AlertType.INFORMATION);
    }

    /**
     * 错误提示框
     *
     * @param message
     */
    public static void showError(String message) {
        showMessage("Error", message, Alert.AlertType.ERROR);
    }

    /***
     * 双精度格式纬度转txt(00°00′00.00″)不带方向
     * @param num 纬度
     * @return 字符串型
     */
    public static String convertLatMethod(double num) {
        num = Math.abs(num);
        String str = "";
        str = String.valueOf((int) num);
        if (str.length() == 0) {
            str = "00";
        } else if (str.length() == 1) {
            str = "0" + str;
        }
        num = num - ((int) num);
        num = num * 60;
        if (num == 0) {
            str = str + "°00'";
        } else if ((int) num < 10) {
            str = str + "°0" + (int) num + "'";
        } else {
            str = str + "°" + (int) num + "'";
        }
        num = num - ((int) num);
        num = num * 60;
        if (num == 0) {
            str = str + "00.00″";
        } else if ((int) num < 10) {
            String MM = String.format("%.2f", num);
            if (MM.length() <= 1) {
                MM = "0" + MM;
            }
            str = str + MM + "″";
        } else {
            if (Double.parseDouble(String.format("%.2f", num)) == 60) {
                String str1 = str.split("°")[1].split("\'")[0];
                if (Integer.parseInt(str1) + 1 < 10) {
                    str = str.split("°")[0] + "°0" + String.valueOf(Integer.parseInt(str1) + 1) + "'00.00″";
                } else if (Integer.parseInt(str1) + 1 == 59) {
                    str = (Integer.parseInt(str.split("°")[0]) + 1) + "°00'00.00″";
                } else {
                    str = str.split("°")[0] + "°" + String.valueOf(Integer.parseInt(str1) + 1) + "'00.00″";
                }
            } else {
                str = str + String.format("%.2f", num) + "″";
            }
        }
        if (!"".equals(str)) {
            str = str.replace("\'", "′");
        }
        return str;
    }

    /***
     * 双精度格式经度转txt(000°00′00.00″)不带方向
     * @param num 经度
     * @return 字符串型
     */
    public static String convertLonMethod(double num) {
        num = Math.abs(num);
        String str = "";
        str = String.valueOf((int) num);
        if (str.length() == 0) {
            str = "000";
        } else if (str.length() == 1) {
            str = "00" + str;
        } else if (str.length() == 2) {
            str = "0" + str;
        }
        num = num - ((int) num);
        num = num * 60;
        if (num == 0) {
            str = str + "°00'";
        } else if ((int) num < 10) {
            str = str + "°0" + (int) num + "'";
        } else {
            str = str + "°" + (int) num + "'";
        }
        num = num - ((int) num);
        num = num * 60;
        if (num == 0) {
            str = str + "00.00″";
        } else if ((int) num < 10) {
            str = str + "0" + String.format("%.2f", num) + "″";
        } else {
            if (Double.parseDouble(String.format("%.2f", num)) == 60) {
                String str1 = str.split("°")[1].split("\'")[0];
                if (Integer.parseInt(str1) + 1 < 10) {
                    str = str.split("°")[0] + "°0" + String.valueOf(Integer.parseInt(str1) + 1) + "'00.00″";
                } else if (Integer.parseInt(str1) + 1 == 60) {
                    str = (Integer.parseInt(str.split("°")[0]) + 1) + "°00'00.00″";
                } else {
                    str = str.split("°")[0] + "°" + String.valueOf(Integer.parseInt(str1) + 1) + "'00.00″";
                }
            } else {
                str = str + String.format("%.2f", num) + "″";
            }
        }
        if (!"".equals(str)) {
            str = str.replace("\'", "′");
        }
        return str;
    }

    /***
     * txt格式经纬度转双精度格式
     * @param txt
     * @return
     */
    public static double convertMethod(String txt) {
        try {
            if (txt.endsWith("E") || txt.endsWith("N")) {
                txt = txt.substring(0, txt.length() - 1);
            }
            String str = txt.replace("°", " ").replace("'", " ").replace("′", " ").replace("″", " ");

            if ("".equals(str)) return 0;
            if (str.indexOf(' ') < 0) return 0;
            String[] temp = str.split(" ");
            if (null == temp || (temp.length != 3 && temp.length != 2)) return 0;
            int du = Integer.parseInt(temp[0]);
            double num = 0;
            if (temp.length == 2) {
                double fen = Double.parseDouble(temp[1]);
                num = du + fen / 60;
            } else if (temp.length == 3) {
                int fen = Integer.parseInt(temp[1]);
                double miao = Double.parseDouble(temp[2]);
                num = du + (fen * 60 + miao) / 3600;
            }
            return num;
        } catch (Exception ex) {
            return 0;
        }
    }

    /***
     * 获取配置文件properties信息
     * @param key
     * @return
     */
    public static String getConfig(String key) {
        return PropertyUtil.getValue(key, StageManager.path + "/config/config.properties");
    }

    /***
     * 获取XML信息
     * @param nodeName
     * @return
     */
    public static Element getNode(String nodeName) {
        try {
            SAXReader saxReader = new SAXReader();

            Document document = saxReader.read(new File(StageManager.path + "/config/mapConfig.xml"));
            Element root = document.getRootElement();
            return root.element(nodeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     *  将颜色转换为16进行的ARGB
     * @param color
     * @return
     */
    public static int to32BitInteger(String color) {
        color = color.startsWith("0x") ? color.substring(2) : color;
        int alpha = Integer.valueOf(color.substring(6, 8), 16);
        int red = Integer.valueOf(color.substring(0, 2), 16);
        int green = Integer.valueOf(color.substring(2, 4), 16);
        int blue = Integer.valueOf(color.substring(4, 6), 16);
        int i = alpha;
        i = i << 8;
        i = i | red;
        i = i << 8;
        i = i | green;
        i = i << 8;
        i = i | blue;
        return i;
    }

    /***
     * 树状结构拼接
     * @param treeRoot
     * @param list
     * @return
     */
    public static TreeItem<TreeModel> getMergeTree(TreeItem<TreeModel> treeRoot, List<TreeModel> list) {
        String id = treeRoot.getValue().getId() == null ? "" : treeRoot.getValue().getId();
        for (TreeModel model : list) {
            String pId = model.getpId() == null ? "" : model.getpId();
            if (id.equals(pId)) {
                if ("element".equals(model.getType())) {
                    TreeItem<TreeModel> treeGroup = new TreeItem<TreeModel>(model);
                    treeRoot.getChildren().add(treeGroup);
                } else {
                    TreeItem<TreeModel> treeGroup = new TreeItem<TreeModel>(model);
                    getMergeTree(treeGroup, list);
                    treeRoot.getChildren().add(treeGroup);
                }
            }
        }
        return treeRoot;
    }

    /**
     * 利用MD5进行加密
     *
     * @param str 待加密的字符串
     * @return 加密后的字符串
     */
    public static String EncoderByMd5(String str) {
        String md5Str = "";
        try {
            //确定计算方法
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(str.getBytes("gbk"));
            md5Str = toHex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5Str.toLowerCase();
    }

    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    /***
     * 通过等级获取比例尺
     * @param grade
     * @return
     */
    public static double getScaleByGrade(int grade) {
        return getScaleByGrade(String.valueOf(grade));
    }

    /***
     * 通过等级获取比例尺
     * @param grade
     * @return
     */
    public static double getScaleByGrade(String grade) {
        if (StageManager.scaleMap.containsKey(grade)) {
            return Double.parseDouble(StageManager.scaleMap.get(grade).split(":")[1]);
        } else {
            return 0;
        }
    }

    /***
     * 显示比例尺、经纬度
     * @param mapView
     * @param wgsPoint
     */
    public static void showLonLat(MapView mapView, Point wgsPoint) {
        Point point = screenTowgs84(wgsPoint.getX(), wgsPoint.getY());
        ((AhMapController) StageManager.CONTROLLER.get("map")).getLabInfo().setText(MapViewMouseEvent.layerNumProperty.get() + "级 | " +
                convertLonMethod(point.getX()) + " : " + convertLatMethod(point.getY()));
    }

    /***
     * 获取当前地图显示层级
     * @param nowScale
     * @return
     */
    public static String getLayerNum(double nowScale) {
        String strNum = "0";
        for (int i = 1; i <= 18; i++) {
            String minNum = String.valueOf(i);
            String maxNum = String.valueOf(i + 1);
            if (StageManager.scaleMap.get(minNum) != null && StageManager.scaleMap.get(maxNum) != null) {
                double minScale = getScaleByGrade(minNum);
                double maxScale = getScaleByGrade(maxNum);
                if (minScale < nowScale) {
                    strNum = minNum;
                    break;
                } else if (minScale >= nowScale && maxScale < nowScale) {
                    strNum = maxNum;
                    break;
                }
            } else if (StageManager.scaleMap.get(maxNum) == null) {
                strNum = maxNum;
                break;
            }
        }
        if (StringEx.parseInt(strNum) > 1) {
            strNum = String.valueOf(StringEx.parseInt(strNum) - 1);
        }
        return strNum;
    }

    /***
     * 获取树节点
     * @param treeItem
     * @param id
     * @return
     */
    public static TreeItem<TreeModel> getTreeModel(TreeItem<TreeModel> treeItem, String id) {
        TreeItem<TreeModel> treeModel = null;
        if (id.equals(treeItem.getValue().getId())) {
            return treeItem;
        }
        for (TreeItem<TreeModel> item : treeItem.getChildren()) {
            if (id.equals(item.getValue().getId())) {
                treeModel = item;
                break;
            }
            treeModel = getTreeModel(item, id);
            if (treeModel != null) {
                break;
            }
        }
        return treeModel;
    }

    /***
     * 两点计算角度
     * @param startPoint
     * @param endPoint
     * @return
     */
    public static String getAngle(Point startPoint, Point endPoint) {
        double angle = Math.atan2(endPoint.getX() - startPoint.getX(), endPoint.getY() - startPoint.getY()) / Math.PI * 180;
        if (angle < 0) {
            angle += 360;
        }
        DecimalFormat decimalFormat = new DecimalFormat("##0.000");//格式化设置
        return decimalFormat.format(angle);
    }

    /***
     * 获取两点之间的长度和角度
     * @param startPoint
     * @param endPoint
     * @return
     */
    public static double[] getLenAndAngle(Point startPoint, Point endPoint) {
        GeodeticDistanceResult result = GeometryEngine.distanceGeodetic(startPoint, endPoint, new LinearUnit(LinearUnitId.METERS), new AngularUnit(AngularUnitId.DEGREES), GeodeticCurveType.GEODESIC);
        double angle = result.getAzimuth1();
        if (angle < 0) {
            angle += 360;
        }
        return new double[]{result.getDistance(), angle};
    }

    /***
     * 定位居中显示
     * @param point
     */
    public static void locationAndCenter(Point point) {
        locationAndCenter(point, GraphicsOverlayData.getMapView().getMapScale());
    }

    /***
     * 定位居中显示
     * @param point
     * @param scale
     */
    public static void locationAndCenter(Point point, double scale) {
        MapView mapView = GraphicsOverlayData.getMapView();

        Viewpoint vpoint = new Viewpoint(point, scale);

        //Point centerP = mapView.getVisibleArea().getExtent().getCenter();

        //Polyline lineGeometry = new Polyline(new PointCollection(Arrays.asList(centerP, wgs84Toscreen(point.getX(), point.getY()))));
        //double speed = getLengthGeodetic(lineGeometry) / mapView.getMapScale();
        mapView.setViewpointAsync(vpoint, (float) 0.5, AnimationCurve.LINEAR);
    }

    /***
     * 将一个对象的名字相同的字段赋值到新对象中
     * @param oldObj
     * @param newObj
     * @param <T>
     */
    public static <T> void copyObject(T oldObj, T newObj) {
        //获取对象中的所有字段
        List<Field> oldFieldList = new ArrayList<>();
        List<Field> newFieldList = new ArrayList<>();
        getFieldList(oldObj.getClass(), oldFieldList);
        getFieldList(newObj.getClass(), newFieldList);

        Map<String, Field> map = oldFieldList.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
        //赋值
        for (Field newField : newFieldList) {
            if (map.containsKey(newField.getName())) {
                try {
                    Field oldField = map.get(newField.getName());

                    ////设置是否允许访问
                    newField.setAccessible(true);
                    oldField.setAccessible(true);

                    if (!newField.getType().getName().equals("javafx.beans.property.StringProperty") && !newField.getType().getName().equals("java.lang.String") &&
                            !newField.getType().getName().equals("javafx.beans.property.DoubleProperty") && !newField.getType().getName().equals("java.lang.Double") &&
                            !newField.getType().getName().equals("double") && !newField.getType().getName().equals("javafx.beans.property.IntegerProperty") &&
                            !newField.getType().getName().equals("java.lang.Integer") && !newField.getType().getName().equals("int") &&
                            !newField.getType().getName().equals("java.lang.Boolean") && !newField.getType().getName().equals("boolean") &&
                            !newField.getType().getName().equals("java.lang.Float") && !newField.getType().getName().equals("float")) {
                        continue;
                    }

                    if (oldField.getType().getName().equals(newField.getType().getName())) {
                        newField.set(newObj, oldField.get(oldObj));
                    } else if (oldField.getType().getName().equals("javafx.beans.property.StringProperty") && newField.getType().getName().equals("java.lang.String")) {
                        newField.set(newObj, ((StringProperty) oldField.get(oldObj)).get());
                    } else if (newField.getType().getName().equals("javafx.beans.property.StringProperty") && oldField.getType().getName().equals("java.lang.String")) {
                        newField.set(newObj, new SimpleStringProperty((String) oldField.get(oldObj)));
                    } else if (oldField.getType().getName().equals("javafx.beans.property.DoubleProperty") && (newField.getType().getName().equals("java.lang.Double") || newField.getType().getName().equals("double"))) {
                        newField.set(newObj, ((SimpleDoubleProperty) oldField.get(oldObj)).get());
                    } else if (newField.getType().getName().equals("javafx.beans.property.DoubleProperty") && (oldField.getType().getName().equals("java.lang.Double") || oldField.getType().getName().equals("double"))) {
                        newField.set(newObj, new SimpleDoubleProperty((Double) oldField.get(oldObj)));
                    } else if (oldField.getType().getName().equals("javafx.beans.property.IntegerProperty") && (newField.getType().getName().equals("java.lang.Integer") || newField.getType().getName().equals("int"))) {
                        newField.set(newObj, ((SimpleIntegerProperty) oldField.get(oldObj)).get());
                    } else if (newField.getType().getName().equals("javafx.beans.property.IntegerProperty") && (oldField.getType().getName().equals("java.lang.Integer") || oldField.getType().getName().equals("int"))) {
                        newField.set(newObj, new SimpleIntegerProperty((Integer) oldField.get(oldObj)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     * 获取对象中的字段
     * @param clazz
     * @param list
     * @param <T>
     */
    public static <T> void getFieldList(Class<?> clazz, List<Field> list) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.toString().contains("final")) {
                list.add(field);
            }
        }
        if (clazz.getSuperclass() != null && !"java.lang.Object".equals(clazz.getSuperclass().getName())) {
            getFieldList(clazz.getSuperclass(), list);
        }
    }

    /***
     * 获取String类型的时间
     * @param dateTime
     * @return
     */
    public static String getStrDateTime(LocalDateTime dateTime) {
        return dateTime.getYear() + "-" + dateTime.getMonthValue() + "-" + dateTime.getDayOfMonth() + " " + dateTime.getHour() + ":" + dateTime.getMinute();
    }

    /***
     * 显示Web弹出框
     * @param title
     * @param url
     */
    public static void showWeb(String title, String url) {
        try {
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
            WebView webView = new WebView();
            webView.setPrefHeight(680);
            webView.setPrefWidth(1050);
            WebEngine engine = webView.getEngine();
            engine.load(url);
            Scene scene = new Scene(webView); //创建场景；
            Stage stage = new Stage();//创建舞台；
            stage.setScene(scene); //将场景载入舞台；
            stage.setTitle(title);
            stage.initStyle(StageStyle.UTILITY);
            //stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(MainEntry.getStage());
            stage.show(); //显示窗口；
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 颜色转数字
     * @param color 颜色
     * @param bolAlpha 是否需要颜色选择器中的透明度
     * @param alpha 透明度
     * @return
     */
    public static int colorToInt(Color color, boolean bolAlpha, int alpha) {
        int red = (int) Math.round(color.getRed() * 255.0) & 0xFF;
        int green = (int) Math.round(color.getGreen() * 255.0) & 0xFF;
        int blue = (int) Math.round(color.getBlue() * 255.0) & 0xFF;
        alpha = bolAlpha ? (int) Math.round(color.getOpacity() * 255.0) & 0xFF : alpha;
        return ((alpha << 24) | (red << 16) | (green << 8) | (blue << 0));
    }

    /***
     * 数字转颜色
     * @param color
     * @return
     */
    /***
     * 数字转颜色
     * @param color 数字类型的颜色
     * @param bolAlpha 是否需要数据类型颜色的透明度
     * @return
     */
    public static Color intToColor(int color, boolean bolAlpha) {
        int a = bolAlpha ? (byte) (color >> 24) & 0xFF : 0xFF;
        int r = (byte) (color >> 16) & 0xFF;
        int g = (byte) (color >> 8) & 0xFF;
        int b = (byte) (color >> 0) & 0xFF;
        return Color.rgb(r, g, b, a / 255);
    }

    /***
     * 判断是否为数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /***
     * 字符串验证是否是电话
     * @param str
     * @return
     */
    public static boolean isTel(String str) {
        Pattern pattern = Pattern.compile("(^(0\\d{2,3}-\\d{7,8})$)|(^(\\d{7,8})$)|(^1[3578]\\d{9}$)");
        return pattern.matcher(str).matches();
    }

    /***
     * 获取时间
     * @param date
     * @return
     */
    public static Date parseTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(date);
        } catch (Exception e) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                return sdf.parse(date);
            } catch (Exception ex) {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return sdf.parse(date);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
            return null;
        }
    }

    /***
     * 获取规定格式的字符串
     * @param strDate
     * @param format
     * @return
     */
    public static String parseTime(String strDate, String format) {
        Date date = parseTime(strDate);
        if (date != null) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(format);
                return df.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /***
     * 获取规定格式的字符串
     * @param date
     * @param format
     * @return
     */
    public static String parseTime(Date date, String format) {
        if (date != null) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(format);
                return df.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 启动应用程序
     *
     * @return
     */
    public static void startRecordVideo() {
        try {
            java.lang.Runtime.getRuntime().exec(StageManager.path + "/CapView/vscap.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 获取周数
     * @param date
     * @return
     */
    public static String getWeekDay(Date date) {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        try {
            cal.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /***
     * 判断是否不为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        if (str != null && !"".equals(str)) {
            return true;
        }
        return false;
    }

    public static void bindTooltip(final Node node, String strTooltip){
        final Tooltip tooltip = new Tooltip(strTooltip);
        node.setOnMouseMoved(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(!tooltip.isShowing()) {
                    tooltip.show(node, event.getScreenX() - 15, event.getScreenY() + 10);
                }
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                tooltip.hide();
            }
        });
    }

}
