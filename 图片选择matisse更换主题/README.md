# 图片选择matisse更换主题

通过查看matisse的依赖库,得知使用知乎主题时,使用的是`colors_zhihu.xml`文件的颜色,所以我们直接引入matisse依赖库:

~~~~java
implementation 'com.zhihu.android:matisse:0.5.2-beta3'
~~~~

然后在values文件夹中创建`colors_zhihu.xml`文件, 我们app的优先级高,所以使用的就是我们定义好的颜色了.

~~~~xml
<resources>
    <color name="zhihu_primary">#F23535</color>
    <color name="zhihu_primary_dark">#F23535</color>

    <color name="zhihu_album_popup_bg">#FFFFFF</color>
    <color name="zhihu_album_dropdown_title_text">#DE000000</color>
    <color name="zhihu_album_dropdown_count_text">#999999</color>
    <color name="zhihu_album_dropdown_thumbnail_placeholder">#EAEEF4</color>
    <color name="zhihu_album_empty_view">#4D000000</color>

    <color name="zhihu_item_placeholder">#EAEEF4</color>
    <color name="zhihu_item_checkCircle_backgroundColor">#F23535</color>
    <color name="zhihu_item_checkCircle_borderColor">#FFFFFF</color>
    <color name="zhihu_capture">#424242</color>

    <color name="zhihu_page_bg">#FFFFFF</color>
    <color name="zhihu_bottom_toolbar_bg">#FFFFFF</color>

    <color name="zhihu_bottom_toolbar_preview_text">#DE000000</color>
    <color name="zhihu_bottom_toolbar_preview_text_disable">#4D000000</color>
    <color name="zhihu_bottom_toolbar_apply_text">#F23535</color>
    <color name="zhihu_bottom_toolbar_apply_text_disable">#F23535</color>

    <color name="zhihu_preview_bottom_toolbar_back_text">#FFFFFF</color>
    <color name="zhihu_preview_bottom_toolbar_apply_text">#F23535</color>
    <color name="zhihu_preview_bottom_toolbar_apply_text_disable">#F23535</color>

    <color name="zhihu_check_original_radio_disable">#808080</color>
</resources>
~~~~

