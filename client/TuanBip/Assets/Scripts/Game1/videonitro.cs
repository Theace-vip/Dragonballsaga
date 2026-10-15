namespace Game1
{
    using System.Collections;
    using System.Collections.Generic;
    using UnityEngine;
    using UnityEngine.Video;



    public class videonitro : MonoBehaviour
    {
        public GameObject canvasObject;
        public VideoPlayer videoPlayer;
        public static int maxVideoLength;
        public static int maxVideoLength2;
        public SplashScr splashScript;
        public bool IsVideoPlaying
        {
            get { return videoPlayer.isPlaying; }
        }



        void Start()
        {
            // Assume you have already set up the VideoPlayer component
            videoPlayer.Play();

            // Register the OnEndVideo method to be called when the video ends
            videoPlayer.loopPointReached += OnEndVideo;

            // Log the maximum length of the video
            int maxVideoLength = Mathf.RoundToInt((float)videoPlayer.length);
            int maxVideoLength2 = (maxVideoLength * 50) - 15;



            SplashScr.SetIsVideoPlaying(true);

        }

        void OnEndVideo(VideoPlayer vp)
        {
            // This method is called when the video ends
            canvasObject.SetActive(false);
            SplashScr.SetIsVideoPlaying(false);
            // Debug.Log("Video đang phát: " + IsVideoPlaying); 
        }

        public void StopVideo()
        {
            videoPlayer.Stop();
            canvasObject.SetActive(false);
            SplashScr.SetIsVideoPlaying(false);
        }
    }
}