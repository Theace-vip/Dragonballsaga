using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
namespace Game2
{
    public class ButtonUI : MonoBehaviour
    {
        public Button skipButton;

        public void SkipVideo()
        {
            // Gọi phương thức StopVideo từ lớp videonitro
            videonitro videoNitroScript = FindObjectOfType<videonitro>();
            if (videoNitroScript != null)
            {
                videoNitroScript.StopVideo();
            }
        }
    }
}