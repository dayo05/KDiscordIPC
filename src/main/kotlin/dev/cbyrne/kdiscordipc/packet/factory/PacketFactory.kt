/*
 *     KDiscordIPC is a library for interacting with the discord client via IPC
 *     Copyright (C) 2021  Conor Byrne
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cbyrne.kdiscordipc.packet.factory

import dev.cbyrne.kdiscordipc.gson.fromJson
import dev.cbyrne.kdiscordipc.gson.toJson
import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.packet.handler.PacketHandler
import dev.cbyrne.kdiscordipc.packet.handler.impl.both.DispatchPacketHandler
import dev.cbyrne.kdiscordipc.packet.handler.impl.serverbound.SetActivityPacketHandler
import dev.cbyrne.kdiscordipc.packet.handler.impl.clientbound.ErrorPacketHandler
import dev.cbyrne.kdiscordipc.packet.handler.impl.serverbound.HandshakePacketHandler
import java.nio.ByteBuffer

/**
 * A class which handles the encoding and decoding of packets
 */
class PacketFactory {
    private val handlers = setOf(
        DispatchPacketHandler(),
        SetActivityPacketHandler(),
        ErrorPacketHandler(),
        HandshakePacketHandler()
    )

    @Suppress("UNCHECKED_CAST")
    fun <T : Packet> encode(packet: T): ByteArray? {
        val handler = handler(PacketHandler.Capability.ENCODE, packet.opcode) as? PacketHandler<T>
        val data = handler?.encode(packet) ?: return null

        val bytes = (data.toJson() ?: "null").toByteArray()
        val buffer = ByteBuffer.allocate(bytes.size + 2 * Integer.BYTES)

        buffer.putInt(Integer.reverseBytes(packet.opcode))
        buffer.putInt(Integer.reverseBytes(bytes.size))
        buffer.put(bytes)
        return buffer.array()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Packet> decode(bytes: ByteArray): T? {
        val opcode = Integer.reverseBytes(ByteBuffer.wrap(bytes.take(8).toByteArray()).int)
        val data = bytes.takeLast(bytes.size - 8).toByteArray().decodeToString()

        val handler = handler(PacketHandler.Capability.DECODE, opcode) ?: return null
        return handler.decode(data.fromJson()) as? T
    }

    private fun handler(capability: PacketHandler.Capability, opcode: Int) =
        handlers.firstOrNull { it.capabilities.contains(capability) && it.opcode == opcode }
}