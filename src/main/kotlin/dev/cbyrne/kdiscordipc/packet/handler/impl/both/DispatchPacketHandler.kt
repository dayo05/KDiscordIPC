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

package dev.cbyrne.kdiscordipc.packet.handler.impl.both

import dev.cbyrne.kdiscordipc.packet.handler.PacketHandler
import dev.cbyrne.kdiscordipc.packet.impl.both.DispatchPacket

class DispatchPacketHandler : PacketHandler<DispatchPacket> {
    override val opcode = 0x01
    override val capabilities = setOf(PacketHandler.Capability.DECODE)

    @Suppress("UNCHECKED_CAST")
    override fun decode(data: Map<String, Any>): DispatchPacket? {
        val cmd = data["cmd"] as String? ?: return null
        val evt = data["evt"] as String?
        val packetData = data["data"] as Map<String, Any>? ?: return null

        return DispatchPacket(cmd, evt, packetData)
    }
}