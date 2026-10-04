<?php

class Message
{
    public $command;
    private $data;

    public function __construct($command, $data = '')
    {
        $this->command = $command;
        $this->data = $data;
    }

    public function writeByte($value)
    {
        $this->data .= chr($value);
    }

    public function writeShort($value)
    {
        $this->data .= pack('n', $value);
    }

    public function writeInt($value)
    {
        $this->data .= pack('N', $value);
    }

    public function writeFloat($value)
    {
        $this->data .= pack('G', $value);
    }

    public function writeDouble($value)
    {
        $this->data .= pack('E', $value);
    }

    public function writeUTF($value)
    {
        $this->data .= pack('n', strlen($value)) . $value;
    }

    public function getData()
    {
        return $this->data;
    }

    public function cleanup()
    {
        $this->data = null;
    }

}

class MessageSend
{

    public function doSendMessage($dos, $msg)
    {
        try {
            $data = $msg->getData();
            fwrite($dos, chr($msg->command));

            if ($data) {
                $size = strlen($data);
                fwrite($dos, pack('n', $size));
                fwrite($dos, $data);
            } else {
                fwrite($dos, pack('n', 0));
            }
            $msg->cleanup();
        } catch (Exception $e) {
            echo "Error: " . $e->getMessage();
        }
    }
}

class Session
{
    private $socket;

    public function __construct($host, $port)
    {
        $this->socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
        if ($this->socket === false) {
            die("socket_create() failed: " . socket_strerror(socket_last_error()));
        }

        $result = socket_connect($this->socket, $host, $port);
        if ($result === false) {
            die("socket_connect() failed: " . socket_strerror(socket_last_error($this->socket)));
        }
    }

    public function sendMessage($message)
    {
        $dataOutputStream = fopen('php://temp', 'w+b');
        $messageSendCollect = new MessageSend();

        $messageSendCollect->doSendMessage($dataOutputStream, $message);

        rewind($dataOutputStream);

        $messageData = stream_get_contents($dataOutputStream);
        fclose($dataOutputStream);

        $bytesSent = socket_write($this->socket, $messageData, strlen($messageData));
        if ($bytesSent === false) {
            die("socket_write() failed: " . socket_strerror(socket_last_error($this->socket)));
        }
        sleep(1);
        $this->close();
    }

    public function close()
    {
        socket_close($this->socket);
    }
}