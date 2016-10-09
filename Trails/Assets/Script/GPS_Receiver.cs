using System.Collections.Generic;
using UnityEngine;
using HoloToolkit.Unity;
#if NETFX_CORE
using Windows.Devices.Bluetooth.Advertisement;
using System.Runtime.InteropServices.WindowsRuntime;
#endif

public class GPS_Receiver : MonoBehaviour
{
    private Vector3 lastPosition;
    private float threshold = 0.609f;
    private Vector3 lastFootPosition;
    private Queue<GameObject> footsteps = new Queue<GameObject>();
#if NETFX_CORE
        BluetoothLEAdvertisementWatcher watcher;
        public static ushort BEACON_ID = 1775;
#endif
    private EventProcessor eventProcessor;

    void Awake()
    {
        eventProcessor = GameObject.FindObjectOfType<EventProcessor>();
#if NETFX_CORE
         watcher = new BluetoothLEAdvertisementWatcher();
            var manufacturerData = new BluetoothLEManufacturerData
            {
                CompanyId = BEACON_ID
            };
            watcher.AdvertisementFilter.Advertisement.ManufacturerData.Add(manufacturerData);

            watcher.Received += Watcher_Received;
            watcher.Start();
#endif
    }

#if NETFX_CORE
    private async void Watcher_Received(BluetoothLEAdvertisementWatcher sender, BluetoothLEAdvertisementReceivedEventArgs args)
    {
        //ushort identifier = args.Advertisement.ManufacturerData[0].CompanyId;
        //byte[] data = args.Advertisement.ManufacturerData[0].Data.ToArray();
        
        //eventProcessor.QueueEvent(() =>
        //{    
        //    GPS_DataPacket packet = GPS_DataPacket.ParseDataPacket(data);
        //    Vector3 distance = new Vector3(0, 0, 0);
        //    distance = Quaternion.Euler(0, Camera.main.transform.localEulerAngles.y, 0) * distance;
        //    GameObject instance = Instantiate(Resources.Load("footprint_object", typeof(GameObject))) as GameObject;
        //    instance.transform.position = GazeManager.Instance.Position + distance + GazeManager.Instance.Normal * 0.001f;
        //    footsteps.Enqueue(instance);

        //    if (footsteps.Count > 1)
        //    {
        //        Destroy(footsteps.Dequeue());
        //    }

            
        //    instance.transform.Rotate(new Vector3(0, (Camera.main.transform.localEulerAngles.y - (float) packet.Latitude), 0));
        //        //Debug.Log(packet.ToString());
        //    });
    }
#endif

}
