package licenses.bus

import com.template.mvvm.actor.Detail
import com.template.mvvm.actor.Message

class CloseExpandableListGroupEvent(val groupIndex: Int) : Message<Detail<Int>> {
    override fun getDetail() = Detail(groupIndex)
}