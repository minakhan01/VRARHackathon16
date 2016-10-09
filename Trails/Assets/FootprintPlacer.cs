using UnityEngine;
using System.Collections;
using HoloToolkit.Unity;
using System.Collections.Generic;
using UnityEngine.VR.WSA.Input;

public class FootprintPlacer : MonoBehaviour {

    private Vector3 lastPosition;
    private float threshold = 0.609f;
    private Vector3 lastFootPosition;
    private Queue<GameObject> footsteps = new Queue<GameObject>();
    private GestureRecognizer mGestureRecognizer;


    // Use this for initialization
    void Start () {
        lastPosition = Camera.main.transform.position;
        lastFootPosition = lastPosition;

        mGestureRecognizer = new GestureRecognizer();
        mGestureRecognizer.SetRecognizableGestures(GestureSettings.Tap);
        mGestureRecognizer.TappedEvent += OnTappedEvent;
        mGestureRecognizer.StartCapturingGestures();
    }

    void OnTappedEvent(InteractionSourceKind source, int tapCount, Ray headRay)
    {
        resetWorld();
    }

    void resetWorld()
    {
        while(footsteps.Count>0) {
            Destroy(footsteps.Dequeue());
        }
    }

    void PlaceFootprint(double angle)
    {
        //Vector3 distance = new Vector3(0, 0, 3.5f);
        //distance = Quaternion.Euler(0, Camera.main.transform.localEulerAngles.y, 0) * distance;
        //GameObject instance = Instantiate(Resources.Load("footprint_object", typeof(GameObject))) as GameObject;
        //instance.transform.position = GazeManager.Instance.Position + distance + GazeManager.Instance.Normal * 0.001f;
        //footsteps.Enqueue(instance);

        //if (footsteps.Count > 8)
        //{
        //    Destroy(footsteps.Dequeue());
        //}

        //instance.transform.Rotate(new Vector3(0, (float) angle, 0));

    }

    // Update is called once per frame
    void Update () {
            Vector3 distance = new Vector3(0, 0, threshold);

            distance = Quaternion.Euler(0, Camera.main.transform.localEulerAngles.y, 0) * distance;

        if (Mathf.Abs(Vector3.Distance(lastPosition,Camera.main.transform.position)) >= threshold)
        {
            lastPosition = Camera.main.transform.position;
            GameObject instance = Instantiate(Resources.Load("footprint_object", typeof(GameObject))) as GameObject;

            if (GazeManager.Instance.Hit)
            {
                instance.transform.position = GazeManager.Instance.Position  + GazeManager.Instance.Normal * 0.001f ;
                lastFootPosition = instance.transform.position;
            } else
            {
                instance.transform.position = lastFootPosition + distance;
                lastFootPosition = instance.transform.position;
            }

            footsteps.Enqueue(instance);
            //if(footsteps.Count > 25)
            //{
            //    Destroy(footsteps.Dequeue());
            //}

            
            instance.transform.Rotate(new Vector3(0, Camera.main.transform.localEulerAngles.y, 0));

        }
    }
}
