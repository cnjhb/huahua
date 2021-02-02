package asia.jhb.huahua

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import asia.jhb.huahua.databinding.FragmentVoltageItemBinding
import asia.jhb.huahua.databinding.FragmentVoltageTableBinding
import asia.jhb.huahua.databinding.VoltageItemBinding

class VoltageTableFragment: Fragment() {
    lateinit var binding:FragmentVoltageTableBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_voltage_table,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem)=when(item.itemId){
        R.id.write_change->{
            CurrentDevice.setBoot()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_voltage_table,container,false)
        binding.recylerView.layoutManager=LinearLayoutManager(activity)
        binding.addFab.setOnClickListener { val intent=VoltageItemActivity.newIntent(activity!!)
            startActivity(intent) }
        updateUI()
        return binding.root
    }
    private inner class VoltageItemHolder(val binding: VoltageItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.containerVoltage.setOnClickListener {
                val intent=VoltageItemActivity.newIntent(activity!!,CurrentDevice.getVotageTable()!!.getIndex(binding.item!!))
            startActivity(intent)}
            binding.containerVoltage.setOnLongClickListener { AlertDialog.Builder(activity!!).setMessage(R.string.warning_delete)
                .setPositiveButton(android.R.string.ok){ dialogInterface: DialogInterface, i: Int -> CurrentDevice.getVotageTable()!!.remove(binding.item!!);updateUI()}.
                setNegativeButton(android.R.string.cancel){ dialogInterface: DialogInterface, i: Int -> }.setTitle("${binding.item!!.frequency}").show();true}
        }
    }
    private inner class VoltageAdatper : RecyclerView.Adapter<VoltageItemHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoltageItemHolder {
            val inflater=LayoutInflater.from(activity)
            val binding=DataBindingUtil.inflate<VoltageItemBinding>(inflater,R.layout.voltage_item,parent,false)
            return VoltageItemHolder(binding)
        }
        override fun onBindViewHolder(holder: VoltageItemHolder, position: Int) {
            holder.binding.item= CurrentDevice.getVotageTable()?.get(position) ?: VoltageItem(0,VoltageLevel.ERRO)
        }
        override fun getItemCount()= CurrentDevice.getVotageTable()?.size() ?:0
    }
    fun updateUI(){
        binding.recylerView.adapter=VoltageAdatper()
    }

    override fun onResume() {
        updateUI()
        super.onResume()
    }
}