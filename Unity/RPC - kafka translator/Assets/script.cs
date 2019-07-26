using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using System.Text;
using Confluent.Kafka;
using Confluent.Kafka.Serialization;

public class script : MonoBehaviour {

    bool piece_selected = false;
    string to;
    string from;
    bool white = true;


    // Use this for initialization
    void Start () {
        //start_server();
        start_client();
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    // Kafka
    public void Send(string from, string to, string topic)
    {
        var config = new Dictionary<string, object>
            {
                { "bootstrap.servers", "192.168.160.210:9092"}
            };
        try
        {
            using (var producer = new Producer<string, string>(config, new StringSerializer(Encoding.UTF8), new StringSerializer(Encoding.UTF8)))
            {
                Debug.Log("criei e vou enviar!");
                var dr = producer.ProduceAsync(topic, from ,to ).Result;
                Debug.Log("enviei");
                //output.text = output.text + "Delivered " + dr.Value + " to: " + dr.TopicPartitionOffset + "\n";
                //Debug.Log($"Delivered '{dr.Value}' to: {dr.TopicPartitionOffset}");
            }
        }
        catch(Exception e)
        {
            Debug.Log("correu mal!");
        }

    }

    IEnumerator Receive(string topic)
    {
        Debug.Log("vou começar a ouvir!");
        var conf = new Dictionary<string, object>
            {
              { "group.id", "test-consumer-group" },
              { "bootstrap.servers", "192.168.160.210:9092"},
              { "auto.commit.interval.ms", 5000 },
              { "auto.offset.reset", "earliest" }
            };

        using (var consumer = new Consumer<string, string>(conf, new StringDeserializer(Encoding.UTF8), new StringDeserializer(Encoding.UTF8)))
        {
            consumer.OnMessage += (_, msg)
              => SendRPC(msg.Key, msg.Value);
            
            //Debug.Log($"Read '{msg.Value}' from: {msg.TopicPartitionOffset}");
            //output.text = output.text + "Read " + msg.Value + " from: " + msg.TopicPartitionOffset + "\n";

            consumer.OnError += (_, error)
              => Debug.Log($"Error: {error}");
            //output.text = output.text + "Error: " + error + "\n";

            consumer.OnConsumeError += (_, msg)
              => Debug.Log($"Consume error ({msg.TopicPartitionOffset}): {msg.Error}");
            //output.text = output.text + "Consume error ( " + msg.TopicPartitionOffset + " ): " + msg.Error + "\n";

            consumer.Subscribe(topic);

            while (true)
            {
                consumer.Poll(TimeSpan.FromMilliseconds(50));
                yield return null;
            }
        }

    }

    public void buttonReceive()
    {
        StartCoroutine("Receive", "chess-white-moves");
        StartCoroutine("Receive", "chess-black-moves");

    }

    // RPC - interage com o jogo unity 
    void start_server()
    {
        if (Network.peerType == NetworkPeerType.Server)
        {
            Network.Disconnect();
            Debug.Log("Server off");

        }
        else
        {
            if (Network.peerType == NetworkPeerType.Disconnected)
            {
                Network.InitializeServer(10, 19995, false);
                Debug.Log("Server on");

            }
        }
    }

    void start_client()
    {
        if (Network.peerType == NetworkPeerType.Disconnected)
        {
            var serverIP = "192.168.32.222";

            Network.Connect(serverIP, 19995);
            Debug.Log("Connecting to Server - " + serverIP);

        }
        else
        {
            if (Network.peerType == NetworkPeerType.Client)
            {
                Network.Disconnect();
                Debug.Log("Disconnected from Server");
            }
        }

    }

    void SendRPC(string from, string to)
    {
        Debug.Log("recebi uma mensagem do kafka");
        //GetComponent<NetworkView>().RPC("true_tile_selected", RPCMode.Others, from  );
        //GetComponent<NetworkView>().RPC("true_tile_selected", RPCMode.Others, to );
    }


    [RPC]
    void teste(string s)
    {
        Debug.Log("hello! " + s);
    }

    [RPC]
    void true_tile_selected(string name)
    {
        Debug.Log("tile selected" + name);
        if (!piece_selected)
        { // se ainda não calculámos uma jogada
            piece_selected = true;
            from = name;
        }
        else
        {
            to = name;
            piece_selected = false;

            if (white)
            {
          
                Send(from.ToLower(),to.ToLower(), "chess-white-moves");
            }
            else
            {
                Send(from.ToLower(), to.ToLower(), "chess-black-moves");
            }
             
            white = !white;

        }
    }
    [RPC]
    void true_reset_colors()
    {
        piece_selected = false;
    }
}
