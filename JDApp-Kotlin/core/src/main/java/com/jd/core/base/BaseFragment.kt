package com.jd.core.base


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import com.gyf.barlibrary.ImmersionBar
import com.jd.core.R
import com.jd.core.utils.AppLog
import com.jd.core.view.NavigationBar


abstract class BaseFragment : Fragment() {
    protected var mContext: Context? = null
    var mImmersionBar: ImmersionBar? = null

    //导航
    lateinit var navigationBar: NavigationBar

    //contentView
    lateinit var mContentView: View

    /**
     * 是否启用懒加载，此属性仅对BaseLazyLoadFragment有效
     */
    private var isLazyLoad: Boolean = false
    set(value) {field = value}

    private  var isPrepare: Boolean = false

    /***************************** 抽象方法  */
    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract val layoutId: Int

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.base_layout, container, false)
        //导航
        this.navigationBar = view.findViewById(R.id.navigation_bar)
        //隐藏导航
        if (this.preferredNavigationBarHidden() == true) {
            this.navigationBar.visibility = View.GONE
        }

        val layout_id = this.layoutId
        if (layout_id > 0) {
            //添加子contentView
            val rl = view.findViewById<RelativeLayout>(R.id.base_content)
            val contentView = inflater.inflate(layout_id, null, false)
            rl.addView(contentView)
            this.mContentView = contentView
        }
        //沉浸式状态栏
        initImmersionBar()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    /**
     * 返回view
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepare = true;
        initView(view)
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isLazyLoad) {
            onVisibleToUser()
        }
    }

    override fun onResume() {
        super.onResume()
        AppLog.i("当前运行的fragment:" + javaClass.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mImmersionBar != null) {
            mImmersionBar!!.destroy()
        }
    }

    /**
     * 判断是否需要加载数据
     */
    private fun onVisibleToUser() {
        // 如果已经初始化完成，并且显示给用户
        if (isPrepare && getUserVisibleHint()) {
            loadData();
        }
    }


    private fun initImmersionBar() {
        if (mImmersionBar == null) {
            mImmersionBar = ImmersionBar.with(this)
            mImmersionBar!!.init()
        }
    }

    /**
     * 隐藏导航
     */
    protected open fun preferredNavigationBarHidden(): Boolean {
        return false
    }

    /**
     * 初始化数据
     */
    protected abstract fun initView(view: View)

    /**
     * 懒加载数据
     */
    protected  abstract  fun loadData()

}
