# ConstraintLayout百分比布局

[TOC]

## 百分比间距

~~~~xml
<TextView
    android:id="@+id/tv_name"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="1111111"
    app:layout_constraintHorizontal_bias="0.3"
    app:layout_constraintVertical_bias="0.5"
    app:layout_constraintHorizontal_chainStyle="spread"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    />
~~~~

`app:layout_constraintHorizontal_bias="0.3"`表示横向间距占父控件宽的百分比;

` app:layout_constraintVertical_bias="0.5"`表示纵向间距占父控件高的百分比.

注意:使用这两个属性必须要使用如下4个属性,即水平竖直居中属性:

~~~~xml
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintRight_toRightOf="parent"
~~~~



## 设置图片宽高比

~~~~xml
<ImageView
    android:id="@+id/imageView2"
    android:layout_width="0dp"
    android:layout_height="0dp"
    android:background="@drawable/sfzz"
    app:layout_constraintDimensionRatio="376:236"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"/>
~~~~

