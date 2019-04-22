package com.wram.myframeworkdemo

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.constans.Constant
import com.navigation.PageFragment
import com.navigation.TabActivity
import com.network.NetworkMonitor
import com.widgets.ToastCompat
import com.wram.myframeworkdemo.content.ContentFragment
import com.wram.myframeworkdemo.databinding.ActivityMainBinding
import com.wram.myframeworkdemo.home.HomeFragment
import com.wram.myframeworkdemo.me.MeFragment

/**
 * @author ysk
 * @class describe  [.]
 * @time 2019/4/20 下午2:48
 */
class MainActivity : TabActivity(), NetworkMonitor.NetStatusObserver {

    fun onClick(v: View?) {
        when (v?.id) {
            R.id.home_tab_one ->
                updateContent(Constant.TAB_INDEX_FRIST)
            R.id.home_tab_two ->
                updateContent(Constant.TAB_INDEX_SECOND)
            R.id.home_tab_three ->
                updateContent(Constant.TAB_INDEX_THIRD)
        }
    }

    private var mBinding: ActivityMainBinding? = null

    private var mExitTime: Long = 0

    override val tabsCount: Int
        get() = Constant.DEFAULT_TAB_COUNT

    override val tabContainerId: Int
        get() = R.id.home_main_container

    override val defaultTabIndex: Int
        get() = Constant.TAB_INDEX_FRIST


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initGlobalViews()
    }

    private fun initGlobalViews() {
        NetworkMonitor.instance?.addNetStatusObserver(this)
        mBinding?.homeTabContainer?.check(R.id.home_tab_one)
    }


    fun updateContent(selectedIndex: Int) {
        switchTab(selectedIndex)
    }

    override fun getTabAtIndex(index: Int): PageFragment {
        when (index) {
            Constant.TAB_INDEX_FRIST ->

                return HomeFragment()
            Constant.TAB_INDEX_SECOND ->

                return ContentFragment()
            Constant.TAB_INDEX_THIRD ->

                return MeFragment()
        }

        return HomeFragment()

    }

    override fun observer(available: Boolean, currentNetStatus: Int) {

    }

    override fun onResume() {
        super.onResume()
        switchTab(getCurrentTabIndex())
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastCompat.makeText(this, getString(R.string.common_tips_01), Toast.LENGTH_SHORT).show()
                mExitTime = System.currentTimeMillis()
            } else {
                finish()
                System.exit(0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
