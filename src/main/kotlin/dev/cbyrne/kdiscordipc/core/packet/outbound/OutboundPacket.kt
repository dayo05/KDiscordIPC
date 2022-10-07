package dev.cbyrne.kdiscordipc.core.packet.outbound

import dev.cbyrne.kdiscordipc.core.packet.serialization.OutboundPacketSerializer
import dev.cbyrne.kdiscordipc.core.validation.Validated
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = OutboundPacketSerializer::class)
abstract class OutboundPacket : Validated {
    abstract val opcode: Int

    @SerialName("cmd")
    abstract val cmd: String

    @SerialName("args")
    abstract val args: Arguments?

    abstract var nonce: String

    @Serializable
    open class Arguments : Validated {
        override fun validate() {}
    }

    override fun validate() {}
}

@Serializable
abstract class IrregularOutboundPacket : OutboundPacket() {
    abstract override val opcode: Int

    override var nonce = ""
    override val cmd = ""
    override val args: Arguments? = null
}