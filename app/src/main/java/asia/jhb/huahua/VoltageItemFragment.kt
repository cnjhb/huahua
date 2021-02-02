package asia.jhb.huahua

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import asia.jhb.huahua.databinding.FragmentVoltageItemBinding
import java.lang.NumberFormatException

class VoltageItemFragment() : Fragment() {
    companion object{
        val ARG_VOLTAGE_INDEX="voltage_index"
        fun newInstance(index:Int):VoltageItemFragment{
            val args=Bundle()
            args.putInt(ARG_VOLTAGE_INDEX,index)
            val fragment=VoltageItemFragment()
            fragment.arguments=args
            return fragment
        }
    }
    val item:VoltageItem by lazy {if(arguments!=null&&CurrentDevice.getVotageTable()!=null&&arguments!!.getInt(ARG_VOLTAGE_INDEX,-1)!=-1)
        CurrentDevice.getVotageTable()!!.get(arguments!!.getInt(ARG_VOLTAGE_INDEX))
    else VoltageItem()}

    override fun onOptionsItemSelected(item: MenuItem)=when(item.itemId){
        R.id.check_voltage->{
            CurrentDevice.device?.addVoltageItem(this.item)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_voltage_item,menu)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding:FragmentVoltageItemBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_voltage_item,container,false)
        binding.item=item
        binding.etFrequency.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    item.frequency = Integer.valueOf(s.toString())
                }catch(ignore:NumberFormatException){}
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.spLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                for(level in VoltageLevel.values()){
                    if(level.name==activity!!.resources.getStringArray(R.array.voltage_level)[position]){
                        item.voltage=level
                        break
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        var position:Int=-1
        val strarray=activity!!.resources.getStringArray(R.array.voltage_level)
        for(str in strarray.indices)
            if(this.item.voltage.name==strarray[str]){
                position=str
                break
            }
        binding.spLevel.setSelection(position)
        return binding.root
    }
}