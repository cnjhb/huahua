package asia.jhb.huahua

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class SingleFragmentActivity: AppCompatActivity() {
    abstract val fragment:Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        val fm=supportFragmentManager
        fm.findFragmentById(R.id.fragment_container)
            ?:fm.beginTransaction().add(R.id.fragment_container,fragment).commit()
    }
}