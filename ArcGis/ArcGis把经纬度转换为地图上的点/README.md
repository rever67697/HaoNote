~~~~
Point point = new Point(lon, lat);
Point gisPoint = (Point) GeometryEngine.project(point, SpatialReference.create(4326),    SpatialReference.create(102113));
~~~~

10.x的版本。