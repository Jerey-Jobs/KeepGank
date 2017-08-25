package com.jerey.searchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerey.mutitype.ItemViewBinder;
import com.jerey.mutitype.MultiTypeAdapter;
import com.jerey.searchview.data.HistoryHelper;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * searchView
 * <pre>
 *     1.initDatabase()
 *     2.
 * </pre>
 */
public class SearchView extends LinearLayout {
    private static final String TAG = SearchView.class.getSimpleName();
    public static final int CLOSE = 0;
    public static final int OPEN = 1;
    public static final int HISTORY_COUNT = 6;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CLOSE, OPEN})
    public @interface State {
    }

    private Context context;

    private String hintText = "请输入";
    private int backIcon = R.drawable.ic_arrow_back_black_24dp;
    private int cleanIcon = R.drawable.ic_clean_input;
    private int historyIcon = R.drawable.ic_history_black_24dp;
    private int defaultState;
    private int historyTextColor = android.R.color.darker_gray;
    private boolean isOnKeyCleanVisible = true;


    ImageView ivSearchBack;
    EditText etSearch;
    ImageView clearSearch;
    RecyclerView recyclerView;
    CardView cardViewSearch;
    TextView cleanHistory;

    /*整体根布局view*/
    private View mView;

    private MultiTypeAdapter adapter;
    /** 列表item点击监听器 */
    private OnListItemClickListener onListItemClickListener;
    /** 搜索按钮按下监听器 */
    private OnSearchActionListener onSearchActionListener;
    /** 输入变化监听器 */
    private OnInputTextChangeListener inputTextChangeListener;
    /** 搜索返回按下监听器 */
    private OnSearchBackIconClickListener onSearchBackIconClickListener;
    /** 输入变化监听器 */
    private OnCleanHistoryClickListener onCleanHistoryClickListener;

    private List<Object> mItems = new ArrayList<>();
    HistoryHelper mHistoryHelper;
    String mDbType = "default";


    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
        initDatabase();
        getCustomStyle(attrs);
    }

    public void setType(String type) {
        mDbType = type;
    }

    /***
     * 初始化数据库
     */
    public void initDatabase() {
        mHistoryHelper = new HistoryHelper(context);
        loadHistory("");
    }

    /**
     * 加载各项View属性
     * @param context
     */
    private void initView(final Context context) {
        mView = View.inflate(context, R.layout.search_view, this);
        ivSearchBack = mView.findViewById(R.id.iv_search_back);
        etSearch = ButterKnife.findById(mView, R.id.et_search);
        clearSearch = ButterKnife.findById(mView, R.id.clearSearch);
        recyclerView = ButterKnife.findById(mView, R.id.recyclerView);
        cardViewSearch = ButterKnife.findById(mView, R.id.cardView_search);
        cleanHistory = ButterKnife.findById(mView, R.id.clearHistory);

        ivSearchBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSearchBackIconClickListener != null) {
                    KeyboardUtils.hideSoftInput(etSearch, context);
                    onSearchBackIconClickListener.onClick(view);
                } else
                    close();
            }
        });


        adapter = new MultiTypeAdapter(mItems);
        registerData(HistoryBean.class, new HistoryBeanBinder());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        /**
         * 清空历史记录
         */
        cleanHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getItems().clear();
                adapter.notifyDataSetChanged();
                // TODO 数据库清空
                switchCleanHistoryDisplay();
                if (onCleanHistoryClickListener != null)
                    onCleanHistoryClickListener.onClick();
            }
        });


        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputTextChangeListener != null) {
                    inputTextChangeListener.beforeTextChanged(charSequence);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                switchOneKeyCleanIconDisplay(charSequence);
                loadHistory(charSequence.toString().trim());
                if (inputTextChangeListener != null) {
                    inputTextChangeListener.onTextChanged(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (inputTextChangeListener != null) {
                    inputTextChangeListener.afterTextChanged(editable);
                }
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                mHistoryHelper.insertHistory(new HistoryBean(mDbType,
                        textView.getText().toString()));
                if (i == EditorInfo.IME_ACTION_SEARCH && onSearchActionListener != null) {
                    onSearchActionListener.onSearchAction(textView.getText().toString());
                    KeyboardUtils.hideSoftInput(etSearch, context);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 注册类型
     * @param c
     * @param itemViewBinder
     */
    public void registerData(Class c, ItemViewBinder itemViewBinder) {
        adapter.register(c, itemViewBinder);
    }

    /**
     * @param string
     */
    public void loadHistory(final String string) {
        Log.i(TAG, "loadHistory: ");
        Observable.create(new Observable.OnSubscribe<List<HistoryBean>>() {
            @Override
            public void call(Subscriber<? super List<HistoryBean>> subscriber) {
                subscriber.onNext(mHistoryHelper.searchHistoryList(string, mDbType, HISTORY_COUNT));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<HistoryBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<HistoryBean> historyBeen) {
                        Log.i(TAG, "loadHistory: onNext size:" + historyBeen.size());
                        mItems.clear();
                        mItems.addAll(historyBeen);
                        adapter.setItems(mItems);
                        adapter.notifyDataSetChanged();
                        switchCleanHistoryDisplay();
                    }
                });
    }

    /***
     * 设置“清除历史记录”的显示隐藏
     */
    private void switchCleanHistoryDisplay() {
        if (adapter.getItems() == null || adapter.getItems().size() == 0) {
            cleanHistory.setVisibility(GONE);
        } else {
            cleanHistory.setVisibility(VISIBLE);
        }
    }

    /***
     * 设置“一键清除输入”图标的显示隐藏
     */
    private void switchOneKeyCleanIconDisplay(CharSequence charSequence) {
        if (isOnKeyCleanVisible && !TextUtils.isEmpty(charSequence)) {
            clearSearch.setVisibility(VISIBLE);
        } else {
            clearSearch.setVisibility(GONE);
        }
    }

    /**
     * 初始化自定义属性
     * @param attrs
     */
    public void getCustomStyle(AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchView);

        hintText = a.getString(R.styleable.SearchView_hintText);
        backIcon = a.getResourceId(R.styleable.SearchView_backIcon, backIcon);
        defaultState = a.getInt(R.styleable.SearchView_defaultState, CLOSE);
        cleanIcon = a.getResourceId(R.styleable.SearchView_oneKeyCleanIcon, cleanIcon);
        historyIcon = a.getResourceId(R.styleable.SearchView_historyIcon, historyIcon);
        historyTextColor = a.getColor(R.styleable.SearchView_historyTextColor, ContextCompat
                .getColor(context, historyTextColor));


        setHintText(hintText);
        setBackIcon(backIcon);
        defaultState(defaultState == CLOSE ? CLOSE : OPEN);
        setOneKeyCleanIcon(cleanIcon);
        setHistoryIcon(historyIcon);
        setHistoryTextColor(historyTextColor);
        a.recycle();
    }

    /**
     * 设置一键清除是否显示
     * @param visible
     */
    public void setOneKeyCleanIsVisible(boolean visible) {
        isOnKeyCleanVisible = visible;
        //        if (visible) {
        //            clearSearch.setVisibility(VISIBLE);
        //        } else {
        //            clearSearch.setVisibility(GONE);
        //        }
    }

    /***
     * 设置搜索框提示文本
     * @param hintText
     */
    public void setHintText(String hintText) {
        etSearch.setHint(hintText);
    }

    /**
     * 设置搜索框初始文本
     * @param text
     */
    public void setSearchEditText(String text) {
        etSearch.setText(text);
    }

    /**
     * 设置输入框打开或者关闭状态
     * @param enabled
     */
    public void setSearchEditTextEnabled(boolean enabled) {
        etSearch.setEnabled(enabled);
    }

    /***
     * 设置返回按钮图标
     * @param icon
     */
    public void setBackIcon(@DrawableRes int icon) {
        ivSearchBack.setImageResource(icon);
    }

    /***
     * 设置清空按钮图标
     * @param icon
     */
    public void setOneKeyCleanIcon(@DrawableRes int icon) {
        clearSearch.setImageResource(icon);
    }

    public void setHistoryIcon(@DrawableRes int icon) {
        // adapter.setHistoryIcon(icon);
    }

    /***
     * 设置搜索历史文本颜色
     * @param color
     */
    public void setHistoryTextColor(@ColorInt int color) {
        //   adapter.setHistoryTextColor(color);
    }

    /***
     * 显示搜索框
     */
    public void open() {
        SearchViewUtils.open(context, cardViewSearch, etSearch);
        switchCleanHistoryDisplay();
        switchOneKeyCleanIconDisplay("");
    }

    /***
     * 关闭搜索框
     */
    public void close() {
        SearchViewUtils.close(context, cardViewSearch, etSearch);
    }

    /***
     * 打开或关闭搜索框
     */
    public void autoOpenOrClose() {
        SearchViewUtils.autoOpenOrClose(context, cardViewSearch, etSearch);
    }

    /***
     * 默认状态：显示或隐藏
     * @param state
     */
    public void defaultState(@State int state) {
        switch (state) {
            case CLOSE:
                cardViewSearch.setVisibility(INVISIBLE);
                break;
            case OPEN:
                cardViewSearch.setVisibility(VISIBLE);
                break;
        }
        switchCleanHistoryDisplay();
        switchOneKeyCleanIconDisplay("");
    }

    /***
     * 添加一条历史纪录
     * @param historyBean
     */
    public void addOneHistory(Object historyBean) {
        mItems.add(historyBean);
        adapter.notifyDataSetChanged();
        switchCleanHistoryDisplay();
    }

    /***
     * 添加历史纪录列表
     * @param list
     */
    public void addHistoryList(List<Object> list) {
        mItems.addAll(list);
        adapter.notifyDataSetChanged();
        switchCleanHistoryDisplay();
    }

    /***
     * 设置全新记录列表
     * @param list 历史纪录列表
     */
    public void setListObjects(List<? extends Object> list) {
        mItems.clear();
        mItems.addAll(list);
        adapter.setItems(list);
        adapter.notifyDataSetChanged();
        switchCleanHistoryDisplay();
    }

    /***
     * 搜索框是否打开
     * @return
     */
    public boolean isOpen() {
        return cardViewSearch.getVisibility() == VISIBLE;
    }

    /***
     * 设置历史纪录点击事件
     * @param listItemClickListener
     */
    public void setHistoryItemClickListener(OnListItemClickListener listItemClickListener) {
        this.onListItemClickListener = listItemClickListener;
        adapter.setOnItemClickedListener(new MultiTypeAdapter.onItemClickedListener() {
            @Override
            public void onItemClicked(RecyclerView.ViewHolder holder, int position) {
                if (onListItemClickListener != null) {
                    onListItemClickListener.onClick(mItems.get(position), position);
                }
            }
        });
    }

    /***
     * 设置软键盘搜索按钮点击事件
     * @param onSearchActionListener
     */
    public void setOnSearchActionListener(OnSearchActionListener onSearchActionListener) {
        this.onSearchActionListener = onSearchActionListener;
    }

    /***
     * 设置输入文本监听事件
     * @param onInputTextChangeListener
     */
    public void setOnInputTextChangeListener(OnInputTextChangeListener onInputTextChangeListener) {
        this.inputTextChangeListener = onInputTextChangeListener;
    }

    public void setOnSearchBackIconClickListener(OnSearchBackIconClickListener
                                                         onSearchBackIconClickListener) {
        this.onSearchBackIconClickListener = onSearchBackIconClickListener;
    }

    /**
     * 设置清除历史点击监听
     * @param onCleanHistoryClickListener
     */
    public void setOnCleanHistoryClickListener(OnCleanHistoryClickListener
                                                       onCleanHistoryClickListener) {
        this.onCleanHistoryClickListener = onCleanHistoryClickListener;
    }

    public interface OnListItemClickListener {
        void onClick(Object data, int position);
    }

    public interface OnSearchActionListener {
        void onSearchAction(String searchText);
    }

    public interface OnInputTextChangeListener {
        void onTextChanged(CharSequence charSequence);

        void beforeTextChanged(CharSequence charSequence);

        void afterTextChanged(Editable editable);
    }

    public interface OnSearchBackIconClickListener {
        void onClick(View view);
    }

    public interface OnCleanHistoryClickListener {
        void onClick();
    }
}
