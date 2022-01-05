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

@file:Suppress("ControlFlowWithEmptyBody")

package dev.cbyrne.kdiscordipc.socket

import dev.cbyrne.kdiscordipc.exceptions.SocketConnectionException
import dev.cbyrne.kdiscordipc.exceptions.SocketDisconnectionException
import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.packet.factory.PacketFactory
import dev.cbyrne.kdiscordipc.socket.impl.UnixSystemSocket
import dev.cbyrne.kdiscordipc.socket.impl.WindowsSystemSocket
import org.newsclub.net.unix.AFUNIXSocket
import java.io.File
import java.io.IOException
import kotlin.concurrent.thread

/**
 * The class which interacts with Discord via unix sockets
 *
 * @see AFUNIXSocket
 * @see SocketListener
 */
class DiscordSocket {
    // TODO: Maybe do something better
    private val socket: SystemSocket =
        if (System.getProperty("os.name").contains("win", true)) {
            WindowsSystemSocket()
        } else {
            UnixSystemSocket()
        }

    private val factory = PacketFactory()

    val isConnected: Boolean
        get() = socket.isConnected

    /**
     * The listener which will handle packets once they are decoded
     */
    var listener: SocketListener? = null

    /**
     * Connects to the Discord IPC socket
     * This method starts a thread which will constantly attempt to read for packets whilst the socket is still connected
     *
     * @throws SocketConnectionException If an error has occurred during the connection
     */
    @Throws(SocketConnectionException::class)
    fun connect() {
        socket.connect(getIpcFile(0))

        thread(start = true) {
            while (isConnected) {
                val packet = readPacket() ?: error("Failed to read packet!")
                listener?.onPacket(packet)
            }
        }
    }

    /**
     * Closes the connection to the unix socket
     *
     * @throws IllegalStateException If the socket is already closed
     * @throws SocketDisconnectionException If an error has occurred when closing this socket
     */
    @Throws(IllegalStateException::class, SocketDisconnectionException::class)
    fun disconnect() = socket.disconnect()


    /**
     * Sends a [Packet] tot the connected [AFUNIXSocket] socket
     *
     * @param packet The packet to send
     * @throws IllegalStateException If the socket is not connected yet
     */
    fun send(packet: Packet) {
        if (!socket.isConnected) throw IllegalStateException("You must connect to the socket before sending packets")

        socket.outputStream?.apply {
            val encodedPacket = factory.encode(packet) ?: throw IllegalStateException("Failed to send $packet")

            try {
                write(encodedPacket)
            } catch (t: Throwable) {
                disconnect()
                listener?.onSocketClosed(t.localizedMessage)
            }
        }
    }

    /**
     * Reads a [Packet] via the input stream
     */
    private fun readPacket(): Packet? {
        if (!socket.isConnected) throw IllegalStateException("You must connect to the socket before reading packets")

        socket.inputStream?.apply {
            while (available() == 0) {
            }

            return try {
                // Weird edge case here, an input stream can be open above, and we can have packets available
                // In the time from that loop to readBytes, the socket can close. Let's have another check here
                // to make sure the socket is still connected before reading bytes
                if (!socket.isConnected) throw IllegalStateException("Socket disconnected while trying to read a packet.")

                val bytes = ByteArray(available())
                read(bytes, 0, available())

                factory.decode(bytes)
            } catch (t: Throwable) {
                disconnect()
                listener?.onSocketClosed(t.localizedMessage)

                null
            }
        }

        return null
    }

    /**
     * Returns a path to the IPC socket depending on the platform
     *
     * If on Windows, "\\?\pipe\discord-ipc-index" will always be returned
     * If on a Unix system, "%tempdir%/discord-ipc-index" will be returned
     *
     * @throws IOException On unix if a temporary directory could not be found
     * @see [getDefaultTempDir]
     */
    @Suppress("SameParameterValue")
    @Throws(IOException::class)
    private fun getIpcFile(rpcIndex: Int): File {
        val platform = System.getProperty("os.name").lowercase()

        return if (platform.contains("win")) {
            File("\\\\?\\pipe\\discord-ipc-$rpcIndex")
        } else {
            val systemTempDir =
                System.getenv("XDG_RUNTIME_DIR") ?: System.getenv("TMPDIR") ?: System.getenv("TMP")
                ?: System.getenv("TEMP")

            File(systemTempDir ?: getDefaultTempDir(), "discord-ipc-${rpcIndex}")
        }
    }

    /**
     * Gets the temporary directory path for the system.
     * This should only be used if XDG_RUNTIME_DIR, TMPDIR, TMP or TEMP is not set.
     *
     * @see [File.createTempFile]
     * @throws IOException If the temporary file could not be created
     */
    @Throws(IOException::class)
    private fun getDefaultTempDir() = File.createTempFile("kdiscordipc", "").parent
}
