package com.navigation

import android.os.Bundle
import com.helper.NavigationHelper
import java.util.*

/**
 * Created by yangsk on 2017/5/21.
 */

abstract class TabActivity : BaseActivity() {

    private var mTabsInfo: MutableList<TabInfo>? = null

    private var mCurrentTabIndex: Int = 0

    val currentFragment: PageFragment?
        get() = createFragmentAtIndex(mCurrentTabIndex)

    protected abstract val tabsCount: Int

    protected abstract val tabContainerId: Int

    abstract val defaultTabIndex: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (null != savedInstanceState) {
            mCurrentTabIndex = savedInstanceState.getInt(CURRENT_TAB_INDEX_KEY, defaultTabIndex)
        } else {
            mCurrentTabIndex = defaultTabIndex
        }

        initializeTabs()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENT_TAB_INDEX_KEY, mCurrentTabIndex)
    }

    override fun onDestroy() {
        destroyResource()
        super.onDestroy()
    }

    private fun destroyResource() {
        if (null != mTabsInfo) {
            for (tabInfo in mTabsInfo!!) {
                if (null != tabInfo.pageFragment) {
                    tabInfo.pageFragment = null
                }
            }
            mTabsInfo?.clear()
            mTabsInfo = null
        }
    }

    private fun initializeTabs() {
        val count = tabsCount
        if (null == mTabsInfo) {
            mTabsInfo = ArrayList(count)
        }
        for (i in 0 until count) {
            mTabsInfo?.add(TabInfo())
        }

        val fragment = createFragmentAtIndex(mCurrentTabIndex)
        val transaction = supportFragmentManager.beginTransaction()
        if (fragment != null) {
            transaction.replace(tabContainerId, fragment)
        }
        transaction.commitAllowingStateLoss()
        supportFragmentManager.executePendingTransactions()
    }

    private fun createFragmentAtIndex(index: Int): PageFragment? {
        val tabInfo = mTabsInfo!![index]
        if (null == tabInfo.pageFragment) {
            val fragment = getTabAtIndex(index)
            tabInfo.pageFragment = fragment
        }
        return tabInfo.pageFragment
    }

    protected fun switchTab(index: Int) {
        if (mCurrentTabIndex != index) {
            NavigationHelper.currentPage = index
            val nextTab = mTabsInfo?.get(index)
            val currentTab = mTabsInfo?.get(mCurrentTabIndex)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(0, 0)
            currentTab?.pageFragment?.let { transaction.hide(it) }
            if (null == nextTab?.pageFragment) {
                val fragment = createFragmentAtIndex(index)
                nextTab?.pageFragment = fragment
                if (nextTab != null) {
                    deliverFragmentBeforeSwitch(nextTab, fragment, index)
                    nextTab.pageFragment?.let { transaction.add(tabContainerId, it, fragment?.javaClass?.name) }
                }
            } else {
                val mayRemove = nextTab.pageFragment
                if (nextTab.pageFragment != null && deliverFragmentBeforeSwitch(nextTab, nextTab.pageFragment, index)) {
                    if (mayRemove != null) {
                        transaction.remove(mayRemove)
                    }
                    transaction.add(tabContainerId, nextTab.pageFragment!!, nextTab.pageFragment!!.javaClass.name)
                } else {
                    if (nextTab.pageFragment != null)
                        transaction.show(nextTab.pageFragment!!)
                }
            }
            transaction.commitAllowingStateLoss()
            supportFragmentManager.executePendingTransactions()
            mCurrentTabIndex = index
        }
    }

    private fun deliverFragmentBeforeSwitch(nextTab: TabInfo, pageFragment: PageFragment?, index: Int): Boolean {
        val temp = onFragmentSwitched(pageFragment, index)
        if (null != temp && temp !== nextTab.pageFragment) {
            nextTab.pageFragment = temp
            return true
        }
        return false
    }

    protected fun onFragmentSwitched(fragment: PageFragment?, pageIndex: Int): PageFragment? {
        return fragment
    }

    protected abstract fun getTabAtIndex(index: Int): PageFragment

    fun getCurrentTabIndex(): Int {
        return mCurrentTabIndex
    }

    private class TabInfo {
        internal var pageFragment: PageFragment? = null
    }

    companion object {

        private val CURRENT_TAB_INDEX_KEY = "mCurrentTabIndexKey"
    }
}