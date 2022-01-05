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

package dev.cbyrne.kdiscordipc.packet.impl.both

import dev.cbyrne.kdiscordipc.DiscordIPC
import dev.cbyrne.kdiscordipc.gson.toJson
import dev.cbyrne.kdiscordipc.packet.Packet

/**
 * A packet which is received by the client when an event is being dispatched
 * @see DiscordIPC.onPacket
 */
class DispatchPacket(
    val command: String,
    val event: String?,
    val data: Map<String, Any>
) : Packet {
    override val opcode = 0x01

    override fun toString(): String {
        return mapOf("cmd" to command, "event" to (event ?: "null"), "data" to data).toJson() ?: "{}"
    }
}
