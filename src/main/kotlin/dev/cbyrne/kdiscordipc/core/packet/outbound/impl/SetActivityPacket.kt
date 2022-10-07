package dev.cbyrne.kdiscordipc.core.packet.outbound.impl

import dev.cbyrne.kdiscordipc.core.packet.outbound.OutboundPacket
import dev.cbyrne.kdiscordipc.core.util.validate
import dev.cbyrne.kdiscordipc.data.activity.Activity
import kotlinx.serialization.Serializable

@Serializable
data class SetActivityPacket(
    override val args: Arguments,
    override val cmd: String = "SET_ACTIVITY",
    override val opcode: Int = 0x01,
    override var nonce: String = "0"
) : CommandPacket() {
    constructor(pid: Long, activity: Activity?, nonce: String = "0") : this(Arguments(pid, activity), nonce = nonce)

    @Serializable
    data class Arguments(
        val pid: Long,
        val activity: Activity?
    ) : OutboundPacket.Arguments() {
        override fun validate() = pid.validate() && activity.validate()
    }

    override fun validate() = args.validate()
}
