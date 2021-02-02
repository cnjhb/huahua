package asia.jhb.huahua

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

class VoltageItemActivity() : SingleFragmentActivity() {
    companion object{
        val EXTRA_VOLTAGE_INDEX="asia.jhb.huahua.voltage_index"
        fun newIntent(context: Context,index:Int?=null):Intent{
            val intent=Intent(context,VoltageItemActivity::class.java)
            if(index!=null)
                intent.putExtra(EXTRA_VOLTAGE_INDEX,index)
            return intent
        }
    }
    override val fragment: Fragment by lazy {VoltageItemFragment.newInstance(intent.getIntExtra(EXTRA_VOLTAGE_INDEX,-1))}
}