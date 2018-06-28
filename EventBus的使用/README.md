### EventBus的使用

[TOC]

#### 添加依赖

`compile 'org.greenrobot:eventbus:3.0.0'`

#### 新建一个事件类,包含需要传递的消息

~~~~java
public class Event {
    private int topPosition;
    private int bottomPosition;

    public int getTopPosition() {
        return topPosition;
    }

    public void setTopPosition(int topPosition) {
        this.topPosition = topPosition;
    }

    public int getBottomPosition() {
        return bottomPosition;
    }

    public void setBottomPosition(int bottomPosition) {
        this.bottomPosition = bottomPosition;
    }

    @Override
    public String toString() {
        return "NowMovieDetailEvent{" +
                "topPosition=" + topPosition +
                ", bottomPosition=" + bottomPosition +
                '}';
    }
}
~~~~

#### 发送与接收

​	在需要发送消息的地方发送:

~~~~java
 		Event event = new Event();
        event.setTopPosition(0);
        event.setBottomPosition(1);
        EventBus.getDefault().post(event);
~~~~

在需要接收消息的类中,需要订阅消息和解除订阅消息.(在创建和销毁方法中),如:

~~~~java
  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now, container, false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return view;
    }

 @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }//解除订阅
    }
~~~~

然后在此类中,接收消息:

~~~~java
	@Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onNowMovieDetailEvent(Event event) {
       
    }
~~~~



