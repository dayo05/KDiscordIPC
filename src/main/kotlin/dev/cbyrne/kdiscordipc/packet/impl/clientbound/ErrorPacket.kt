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

package dev.cbyrne.kdiscordipc.packet.impl.clientbound

import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.packet.PacketDirection

/**
 * The packet which the client receives if an error has occurred when connecting to the server
 */
class ErrorPacket(packetData: Map<String, Any>) : Packet {
    override val opcode = 1
    override val direction = PacketDirection.CLIENTBOUND

    val code = packetData["code"] as Double? ?: error("Failed to parse packet: $packetData")
    val message = packetData["message"] as String? ?: error("Failed to parse packet: $packetData")
}