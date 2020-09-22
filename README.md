
![](https://cdn.jsdelivr.net/gh/AngleLwt/images/angel/angle.png)

# 使用步骤

### 添加依赖

![](https://cdn.jsdelivr.net/gh/AngleLwt/images/angel/Screen%20Shot%202020-08-10%20at%209.20.09%20PM.png)

1. `maven { url 'https://jitpack.io' }`

2. `implementation 'com.github.AngleLwt:AngelMvp:Tag'`

### 使用方式

#### 一层
1.创建契约类
 * 创建Presenter继承IBasePresenter<T extend IBaseView> 
 * 创建View继承IBaseView<T extend IBasePresenter>

```java

public interface ConstrantVideo {
//    创建P层继承IBasePresenter
    interface IVideoPresenter extends IBasePresenter<IVideoView> {
    //p传递数据方法
        void getVideoData();


    }
    
// 创建View契约类
    interface IVideoView extends IBaseView<ConstrantVideo.IVideoPresenter> {
        void onSucces(Video video);

        void onFail(String error);

        void onNetError();

        void showLoadLing();

        void closeLoadLing();
    }

}

```

2.创建P层

* BaseRepository创建`model`成员变量在`P`层构造方法通过`new BaseRepository()`得到model实例
* 在`P`层方法中根据请求Url 创建GetRequest或者PostRequest对象
* 如果有参数设置请求参数
* 通过model对象调用`mModel.doRequest(request，new IBaseCallBack<T>{})`获得数据

`T 为你想要请求的bean类`

```java


public class VideoPresenter extends BasePresenter<ConstrantVideo.IVideoView> implements ConstrantVideo.IVideoPresenter {
    private BaseRepository mModel;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public VideoPresenter() {
        mModel = new BaseRepository();
    }

    @Override
    public void getVideoData() {

//    给请求对象添加参数
        GetRequest request = new GetRequest("/app/v_1_6/article/videolist");
// 设置请求参数
    request.setParams(ParamsUtils.getCommonParams());
        request.setType(Video.class);
//   加载动画
        mView.showLoadLing();

        mModel.doRequest(request, new IBaseCallBack<Video>() {
            //            切断cut方法
            @Override
            public void onCut(Disposable disposable) {
                mCompositeDisposable.add(disposable);
            }

            //   获取结果
            @Override
            public void onResult(MvpResponse<Video> data) {
                if (data.getCode() == 1) {
                    mView.onSucces(data.getData());
                    mView.closeLoadLing();
                } else {
                    mView.onFail(data.getMsg());
                }
            }
        });


    }


    @Override
    public boolean cancelRequest() {
         return true;
    }
}


```

3 Fragmenrt 
1. 继承BaseMvpFragment<T extend `IBasePresenter`>implements具体的`View`

2. 在成功失败方法得到数据

```java

 public class VideoFragmenrt extends BaseMvpFragment<ConstrantVideo.IVideoPresenter> implements ConstrantVideo.IVideoView {


    @Override
    protected int getLayoutView() {

        return R.layout.fragment_video_fragmenrt;
    }

    @Override
    public void onSucces(Video video) {
        toast(video.getList().get(2).getShare_link());
    }

    @Override
    public void onFail(String error) {
        toast(error);
    }

    @Override
    public void onNetError() {
        toast("网络错误");
    }

    @Override
    public void showLoadLing() {
        showPopLoading();
    }

    @Override
    public void closeLoadLing() {
        closeLoading();
    }

    @Override
    public ConstrantVideo.IVideoPresenter creatPresenter() {
        return new VideoPresenter();
    }

    @Override
    protected void initView() {
       
        mPresenter.getVideoData();

    }


}

```

#### 二层

 `getLayoutId（）方法中绑定xml`

1. 创建根据请求Url 创建GetRequest或者PostRequest对象
2. 设置请求参数
3. 继承BaseSmartFramgent<T> 范型为实体bean
4. 在onResult（）得到请求数据

```java

public class VideoFragment extends BaseSmartFramgent<Video> {


    @Override
    protected void initView() {

        MvpRequest<Video> request = new GetRequest("/app/v_1_6/article/videolist");//请求方式及Url
        request.setParams(ParamsUtils.getCommonParams());//请求参数
        mPresenter.doRequest(request);//做请求

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return null;
    }

    @Override
    public IBasePresenter getPresenter() {
        return null;
    }

// 接受数据结果
    @Override
    public void onResult(MvpResponse<Video> response) {
        if (response.getCode()==1){
            showPopLoading();
            showToast(response.getData().getList().get(2).getShare_link());
            closeloading();
        }else{
            showPopLoading();
            showToast(response.getMsg());
            closeloading();
        }
    }
    //  设置Fragment初始化动画
    @Override
    public int getExitLeftAnimation() {
        return 0;
    }

    @Override
    public int getEnterRightAnimation() {
        return 0;
    }


//  对上一个Fragment进行处理
    @Override
    public Action dealWithProFragment() {
        return Action.NONE;
    }
}

```

# SuperMVP架构目录

* 第一层 



```
|-data(数据包)
 |-entity（bean）
   -Httresult
 |-net(网络包)
  |-ok（ok数据请求）
    |- convert(json转换器)
    -ApiService（请求Observable）
    -DataService(网络请求封装）
  |-request
   -MvpRequest(请求类封装)
   -GetRequest(GET请求类封装)
   -PostRequest(PostRequest请求类封装)
  |-response
   --MvpResponse(结果类封装)
|-model
 -IBaseCallBack（接口回掉上传数据）
 -IBaseModel(model层契约类)
 -BaseModel(model处理请求数据)
|-presenter
 -IBasePresenter（P层契约类）
 -BasePresenter（P层传递数据）
|-view
 -IBaseView（view层契约类）
 -BaseFramgent
 -BaseActivity
 -MvpBaseFragment
 -MvpBaseActivity
```

- [ ] 具体实现代码


### data

* ApiService

```java

public interface ApiService {
    @POST
    @FormUrlEncoded
    Observable<String> doPost(@Url String url, @FieldMap HashMap<String,Object> params);

    @GET
    Observable<String> doGet(@Url String url, @QueryMap HashMap<String ,Object> params);
}

```

* DataService

```java

public class DataService {
    //    等待时间
    private static final int WAIT_TIME = 10;
    //    全局ApiService
    private static volatile ApiService mApiService;

    //  获得ApiServiec
    public static ApiService getApiService() {
        if (mApiService == null) {
//            锁定线程
            synchronized (DataService.class) {
                if (mApiService == null) {
//                    创建拦截器
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    if (BuildConfig.DEBUG) {
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    } else {
                        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

                    }
//                    创建OK对象
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .readTimeout(WAIT_TIME, TimeUnit.SECONDS)
                            .writeTimeout(WAIT_TIME, TimeUnit.SECONDS)
                            .connectTimeout(WAIT_TIME, TimeUnit.SECONDS)
                            .addInterceptor(interceptor);
//                 创建retrofit对象
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constrant.BASEURL)
                            .addConverterFactory(MvpGsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                            .client(builder.build())
                            .build();
//                 返回ApiService
                    return retrofit.create(ApiService.class);

                }
            }
        }

        return mApiService;
    }
}

```

* MvpRequest

```java


public class MvpRequest<T> {
    protected String Url;//后缀Url
    protected RequestMethod requestMethod;//请求方式
    protected RequestType requestType = RequestType.FIRST;//请求是第一次？刷新？加载更多？
    protected HashMap<String, Object> params;//创建请求参数
    protected Class<T> type;// 创建请求结果具体类型

    protected boolean isEnableCancel;//是否取消请求

    public boolean isEnableCancel() {
        return isEnableCancel;
    }

    public void setEnableCancel(boolean enableCancel) {
        isEnableCancel = enableCancel;
    }

    public Class<T> getType() {

        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    //  创建请求后缀Url
    public MvpRequest(String url) {
        Url = url;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }


}


```

* GetRequest

```java


public class GetRequest<T> extends MvpRequest<T>{
    public GetRequest() {
    }

    public GetRequest(String url) {
        super(url);
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.GET;
    }
}


```

PostRequest

```java

public class PostRequest<T> extends MvpRequest<T> {
    public PostRequest() {
    }

    public PostRequest(String url) {
        super(url);
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }
}

```

### model

* IBaseCallBack

```java

public interface IBaseCallBack<T> {
    void onResult(MvpResponse<T> data);//将数据传入结果对象中

    void onStart(Disposable disposable);//控制model层
}

```


* IBaseModel


```java


public interface IBaseModel {
//获得数据接口
    <T> void doRequest(MvpRequest<T> request, IBaseCallBack<T> callBack);


}


```


* BaseModel

```java

public class BaseModel implements IBaseModel {
    public Consumer consumer = o -> {
    };

    @Override
    public <T> void doRequest(MvpRequest<T> request, IBaseCallBack<T> callBack) {
        doRequest(request, callBack, consumer);
    }
//  实现model契约类
    public <T> void doRequest(MvpRequest<T> request, IBaseCallBack<T> callBack, Consumer<MvpResponse<T>> doBackground) {
        switch (request.getRequestMethod()) {
            case GET:
                doObseverable(request, callBack, doBackground, DataService.getApiService().doGet(request.getUrl(), request.getParams()));
                break;
            case POST:
                doObseverable(request, callBack, doBackground, DataService.getApiService().doPost(request.getUrl(), request.getParams()));

                break;
        }
    }
//   做观察者数据请求
    private <T> void doObseverable(MvpRequest<T> request, IBaseCallBack<T> callBack, Consumer<MvpResponse<T>> doBackground, Observable<String> observable) {
//       判断type类型是否为空
        if (request.getType() == null) {
            Type[] genericInterfaces = callBack.getClass().getGenericInterfaces();
            ParameterizedType genericInterface = (ParameterizedType) genericInterfaces[0];//得到父类的范型化参数
            request.setType((Class<T>) genericInterface.getActualTypeArguments()[0]);
        }


        observable
                .map(jsonData2(request))//解析数据
                .doOnNext(doBackground)//做后台
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MvpResponse<T>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        if (request.isEnableCancel()) {//是否支持去取消请求
                            callBack.onStart(d);
                        }
                    }

                    @Override
                    public void onNext(@NonNull MvpResponse<T> tMvpResponse) {
                        callBack.onResult(tMvpResponse);//得到数据
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBack.onResult(new MvpResponse<T>().setMsg(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private <T> Function<String, MvpResponse<T>> jsonData2(MvpRequest<T> request) {
        return new Function<String, MvpResponse<T>>() {
            @Override
            public MvpResponse<T> apply(String s) throws Throwable {
//          HttpResult<T>
                ParameterizedTypeImpl type = new ParameterizedTypeImpl(HttpResult.class, new Type[]{request.getType()});
                HttpResult<T> data = new Gson().fromJson(s, type);
                if (data.getCode() == 1) {
                    if (data.getData() != null) {
                        return new MvpResponse<T>().setCode(data.getCode()).setData(data.getData());
                    } else {
                        return new MvpResponse<T>().setCode(data.getCode()).setMsg("访问服务器异常");
                    }
                } else {
                    return new MvpResponse<T>().setCode(data.getCode()).setMsg(data.getMessage());
                }

            }
        };
    }
}

```

### Presenter

* IBasePresenter

```java


public interface IBasePresenter<V extends IBaseView> {
    //    绑定View与解绑
    void bindView(V view);

    void unBindView();

    //    是否取消
    boolean cancelRequest();
}


```

* BasePresenter

```java

public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {
//    在BaseP层实现绑定和解绑View
    protected V mView;

    @Override
    public void bindView(V view) {
      mView=view;
    }

    @Override
    public void unBindView() {
      mView=null;
    }

}

```

###  view

* IBaseView

```java

public interface IBaseView<P extends IBasePresenter> {
    P createPresenter();
}

```

* BaseActivity

```java


public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getLayoutId() > 0){
            setContentView(getLayoutId());
            initView();
        }
    }

    protected void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(@StringRes int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }


    protected abstract  int getLayoutId();

    protected abstract void initView();


}

```

* BaseFramgent

```java

public abstract class BaseFramgent extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        Class<? extends View> parentView = view.getRootView().getClass();//获取当前父布局

//        if (parentView == ConstraintLayout.class || parentView == FrameLayout.class || parentView == RelativeLayout.class) {
//        } else {
//            FrameLayout frameLayout = new FrameLayout(getContext());
//            frameLayout.addView(view);
//        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    protected abstract void initView();


    protected abstract int getLayoutId();

    protected <T extends View> T findById(@IdRes int id) {
        return getView().findViewById(id);
    }

    // 吐司
    protected void showToast(@StringRes int msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //    设置Fragment动画
    public int getEnterLeftAnimation() {
        return R.anim.common_page_left_in;
    }

    public int getEnterRightAnimation() {
        return R.anim.common_page_right_in;
    }

    public int getExitLeftAnimation() {
        return R.anim.common_page_left_in;
    }

    public int getExiteRightAnimation() {
        return R.anim.common_page_right_in;
    }

    //是否需要加入回退栈
    public boolean isNeedAddToBackStack() {
        return true;
    }

    // 对上一个Fragment进行处理
    public Action dealWithProFragment() {
        return Action.HIDE;
    }

    public enum Action {
        NONE, HIDE, ATTACH, REMONE
    }


}

```

* MvpBaseActivity

```java



public abstract class MvpBaseActivity<P extends IBasePresenter> extends BaseActivity implements IBaseLoadingView, IBaseView<P> {

    private MvpLoadingView mLoadingView;

    protected P mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.bindView(this);

        loadData();
    }

/*
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mPresenter != null){
                if(mPresenter.cancelRequest()){
                    closeLoading();
                    return true;
                }
            }

        }

        return super.onKeyUp(keyCode, event);

    }*/

    protected abstract void loadData();

    @Override
    public void setLoadView(MvpLoadingView loadView) {
        mLoadingView = loadView;
    }

    @Override
    public MvpLoadingView getLoadingView() {
        return mLoadingView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unBindView();
    }
}


```

* MvpBaseFragment

```java

public abstract class MvpBaseFragment<P extends IBasePresenter> extends BaseFramgent implements IBaseView<P> ,IBaseLoadingView{
      protected P mPresenter;
      protected MvpLoadingView mvpLoadingView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter=createPresenter();
        mPresenter.bindView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unBindView();
    }

    @Override
    public void setLoadView(MvpLoadingView loadView) {
        mvpLoadingView=loadView;
    }

    @Override
    public MvpLoadingView getLoadingView() {
        return mvpLoadingView;
    }
}

```
