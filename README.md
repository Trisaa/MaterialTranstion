
[Android Transition Framework](https://developer.android.com/training/transitions/overview.html) 主要用来做**三件事**：
1. Activity间的转场动画；
2. 不同Activity或Fragment间元素共享，让交互更连贯；
3. 同一个Activity之间一些View的变换动画。

##### 1.Activity转场效果
***
![](https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/transition_A_to_B.png)
当从`ActivityA`切换到`ActivityB`时我们可以跟以前一样分别定义A的退出动画和B的进入动画，5.0之后`android.transition.Transition`提供给我们三个可以直接使用的transition：**Explode**,**Slide**,**Fade**.我们看一下三种效果的对比图。

| Explode | Slide | Fade |
| ------------- |-------------| -----|
| ![](http://7xoww9.com1.z0.glb.clouddn.com/transition_explode.gif) | ![](http://7xoww9.com1.z0.glb.clouddn.com/transition_slide.gif) | ![](http://7xoww9.com1.z0.glb.clouddn.com/transition_fade.gif) |

而你需要做的，首先对Activity的ThemeStyle添加`windowContentTransitions`属性：
```xml
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar"> 
  ...
  <!-- 允许使用transitions -->  
  <item name="android:windowContentTransitions">true</item>  
  <!--是否覆盖执行，其实可以理解成是否同步执行还是顺序执行-->  
  <item name="android:windowAllowEnterTransitionOverlap">false</item>  
  <item name="android:windowAllowReturnTransitionOverlap">false</item>
</style>
```
###### MainActivity.java(部分关键代码)
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Slide slide = new Slide();
    slide.setDuration(1000);
    getWindow().setExitTransition(slide);
}

switch (view.getId()) {   
    case R.id.tv_explode_transition:        
        mIntent.setClass(this, TransitionActivity.class);        
        mIntent.putExtra("transition", "explode");        
        startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        break;
    case R.id.tv_slide_transition:
        mIntent.setClass(this, TransitionActivity.class);
        mIntent.putExtra("transition", "slide");
        startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        break;
    case R.id.tv_fade_transition:
        mIntent.setClass(this, TransitionActivity.class);
        mIntent.putExtra("transition", "fade");
        startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        break;
}
```
用`startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());`来替换之前的startActivity();
###### TransitionActivity.java(部分关键代码)
```java
switch (transition) {
    case "explode":
        imageView.setBackgroundResource(R.drawable.circle_red);
        Explode explode = new Explode();
        explode.setDuration(1000L);
        getWindow().setEnterTransition(explode);
        break;
    case "slide":
        imageView.setBackgroundResource(R.drawable.circle_purple);
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setDuration(1000L);
        getWindow().setEnterTransition(slide);
        break;
    case "fade":
        imageView.setBackgroundResource(R.drawable.circle_blue);
        Fade fade = new Fade();
        fade.setDuration(1000L);
        getWindow().setEnterTransition(fade);
        break;
}
```
让我们来分析以下具体发生了什么：
* 首先ActivityA启动了ActivityB；
* Transition Framework找到A的退出动画（Slide）并且应用；
* Transition Framework找到B的进入动画（Explode）并且应用；
* 返回事件被触发后，Transition Framework执行进入动画和退出动画的逆向过程（但是如果我们定义了returnTransition和reenterTransition动画，返回效果将会按照我们定义的动画执行）。

##### 2.元素共享
***
![](https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/shared_element.png)

![](http://7xoww9.com1.z0.glb.clouddn.com/transition_share_elements.gif) 
 
1.首先我们需要做的仍然是去定义Theme的`android:windowContentTransitions`为true；
2.然后对不同activity或fragment的共享的view元素设置统一的 **android:transitionName**：

>activity_main.xml

```xml
<LinearLayout
    android:id="@+id/tv_share_elements"
    android:layout_width="match_parent"
    android:layout_height="56dp"
    android:background="?android:attr/selectableItemBackground"
    android:orientation="horizontal"
    android:paddingLeft="@dimen/activity_horizontal_margin">
    <ImageView
        android:id="@+id/img_share"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical"
        android:background="@drawable/circle_orange"
        android:transitionName="share" />
    <TextView
        android:id="@+id/tv_share"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical"
        android:layout_marginLeft="12dp"
        android:gravity="center_vertical"
        android:text="Shared Elements"
        android:transitionName="share_text"/>
</LinearLayout>
```
>fragment_share_elements1.xml

```xml
<View
    android:id="@+id/view_top"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:background="@color/color_orange"
    android:transitionName="share">
</View>
<TextView
    android:id="@+id/tv_transition_type"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="@dimen/activity_vertical_margin"
    android:gravity="center"
    android:text="Shared Elements"
    android:transitionName="share_text" />
```
>fragment_share_elements2.xml

```xml
<ImageView
    android:id="@+id/img_share"
    android:layout_width="120dp"
    android:layout_height="120dp"
    android:layout_gravity="center_horizontal"
    android:layout_marginTop="20dp"
    android:background="@drawable/circle_orange"
    android:transitionName="share" />
```
3.使用`ActivityOptions.makeSceneTransitionAnimation()`来定义共享的view和transitionName
>MainActivity.java

```java
mIntent.setClass(this, ShareElementsActivity.class);
ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, findViewById(R.id.img_share), "share");
startActivity(mIntent, transitionActivityOptions.toBundle());
```
>ShareElementsFragment1.java

```java
Slide slideTransition = new Slide(Gravity.LEFT);
slideTransition.setDuration(1000L);
ChangeBounds changeBoundsTransition = new ChangeBounds();
changeBoundsTransition.setDuration(1000L);
sharedElementFragment2.setEnterTransition(slideTransition);
sharedElementFragment2.setAllowEnterTransitionOverlap(true);
sharedElementFragment2.setAllowReturnTransitionOverlap(true);
sharedElementFragment2.setSharedElementEnterTransition(changeBoundsTransition);
getFragmentManager().beginTransaction()
        .replace(R.id.framelayout_container, sharedElementFragment2)
        .addToBackStack(null)
        .addSharedElement(sharedView, "share")
        .commit();
```
如果两个不同界面有多个元素需要共享的话，使用Pair来包装
```java
mIntent.setClass(this, ShareElementsActivity.class);
ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this
        , Pair.create(findViewById(R.id.img_share), "share")
        , Pair.create(findViewById(R.id.tv_share), "share_text"));
startActivity(mIntent, transitionActivityOptions.toBundle());
```

##### 3.水波纹散开效果
这里我们实现了在RevealActivity中两个View的更换，点击右下角切换按钮，水波纹开始散开，第一个View逐渐消失，第二个View逐渐出现。

 ![](http://7xoww9.com1.z0.glb.clouddn.com/transition_reveal.gif)
>activity_reveal.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/ll_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <TextView
        android:id="@+id/tv_reveal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="@dimen/activity_horizontal_margin"
        android:text="acon ipsum dolor amet cupidatat bresaola minim, aliquip beef aute ea porchetta. Meatball brisket do, rump in beef ea ham hock spare ribs mollit qui dolore ipsum voluptate cow. Drumstick prosciutto salami duis jerky jowl. Mollit ball tip short ribs doner fugiat frankfurter leberkas andouille kevin pork loin nostrud ham culpa. Rump pariatur ham hock excepteur picanha pork. Corned beef flank proident shankle rump." />
    <TextView
        android:id="@+id/tv_reveal2"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/color_blue"
        android:padding="@dimen/activity_vertical_margin"
        android:text="This is the content, we set it visible when the animation finished"
        android:textColor="@android:color/white"
        android:textSize="18sp"
        android:visibility="gone" />
    <ImageView
        android:id="@+id/img_green"
        android:layout_width="56dp"
        android:layout_height="56dp"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true"
        android:layout_marginBottom="@dimen/activity_vertical_margin"
        android:layout_marginRight="@dimen/activity_vertical_margin"
        android:background="@drawable/circle_green"
        android:transitionName="share" />
</RelativeLayout>
```
>RevealActivity.java

```java
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reveal);
    relativeLayout = (RelativeLayout) findViewById(R.id.ll_container);
    textView = (TextView) findViewById(R.id.tv_reveal);
    textview2 = (TextView) findViewById(R.id.tv_reveal2);
    findViewById(R.id.img_green).setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                float finalRadius = (float) Math.hypot(relativeLayout.getWidth(), relativeLayout.getHeight());
                Animator anim = ViewAnimationUtils.createCircularReveal(textview2, (int) motionEvent.getRawX(), (int) motionEvent.getRawY(), 0, finalRadius);
                anim.setDuration(1000L);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        textview2.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        //textView.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
                anim.start();
            }
            return false;
        }
    });
}
```
createCircularReveal的各参数代表的具体含义[参照这里](https://developer.android.com/reference/android/view/ViewAnimationUtils.html)。

##### Demo源码
https://github.com/Trisaa/MaterialTranstion

##### 参考资料
https://github.com/lgvalle/Material-Animations

https://github.com/hehonghui/android-tech-frontier/tree/master/others/%E6%B7%B1%E5%85%A5%E6%B5%85%E5%87%BAAndroid%20%E6%96%B0%E7%89%B9%E6%80%A7-Transition-Part-1

##### 其他转场酷炫的动画

[折叠翻页效果](https://github.com/openaphid/android-flip])

[Fragment Transition](https://github.com/DesarrolloAntonio/FragmentTransactionExtended)

[SwitchLayout](https://github.com/jaychou2012/SwitchLayout)
