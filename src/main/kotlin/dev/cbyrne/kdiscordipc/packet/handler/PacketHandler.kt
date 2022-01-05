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

package dev.cbyrne.kdiscordipc.packet.handler

import dev.cbyrne.kdiscordipc.packet.Packet

/**
 * A class responsible for the encoding and decoding of packets
 *
 * @see dev.cbyrne.kdiscordipc.packet.factory.PacketFactory
 */
interface PacketHandler<T: Packet> {
    val opcode: Int
    val capabilities: Set<Capability>

    fun encode(packet: T): Map<*, *>? {
        return null
    }

    fun decode(data: Map<String, Any>): T? {
        return null
    }

    enum class Capability {
        ENCODE,
        DECODE,
    }
}
