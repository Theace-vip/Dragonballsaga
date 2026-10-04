<?php
require_once  $_SERVER['DOCUMENT_ROOT'] . "/ajax/session.php";

class Service
{
    private static $host = "172.20.10.2";
    private static $port = 14445;
    private static $session;

    private $cmdKey = -83;

    public static function gI(): Service
    {
        if (self::$session === null) {
            self::$session = new Session(self::$host, self::$port);
        }
        return new self();
    }

    public function phpClientMessage($type): Message
    {
        $msg = new Message($this->cmdKey);
        $msg->writeUTF("NROTEST");
        $msg->writeByte($type);
        return $msg;
    }

    public function sendBuyWeb($id,$player_id)
    {
        try {
            $msg = $this->phpClientMessage(0);
            $msg->writeInt($id);
            $msg->writeInt($player_id);
            self::$session->sendMessage($msg);
            $msg->cleanup();
        } catch (Exception $e) {
            echo 'Error: ' . $e->getMessage();
        }
    }
}