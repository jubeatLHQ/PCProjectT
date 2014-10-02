using System.Collections.Specialized;
using System.Net;
using System.Text;

namespace ProCon2014.Client
{
    public class ClientLib
    {
        private static readonly string PlayerIDFieldName = "playerid";
        private static readonly string AnswerFieldName = "answer";
        private static readonly string ProblemIdFieldName = "problemid";
        private static readonly string SubmitAnswerLocation = "/SubmitAnswer";
        private static readonly string ProblemLocation = "/problem/";
        private static readonly string ProblemFileNameFormat = "prob{0:D2}.ppm";

        public static string SubmitAnswer(string serverURL, string problemId, string playerid, string ans)
        {
            byte[] res;
            using (var wc = new WebClient())
            {
                var nvc = new NameValueCollection();
                nvc.Add(PlayerIDFieldName, playerid);
                nvc.Add(ProblemIdFieldName, problemId);
                nvc.Add(AnswerFieldName, ans);
                res = wc.UploadValues("http://" + serverURL + SubmitAnswerLocation, nvc);
            }
            return Encoding.UTF8.GetString(res);
        }

        public static byte[] GetProblem(string serverURL, string problemId)
        {
            using (var wc = new WebClient())
            {
                return wc.DownloadData("http://" + serverURL + ProblemLocation + GetProblemFileName(problemId));
            }
        }

        public static string GetProblemFileName(string problemId)
        {
            int numProblemId = int.Parse(problemId);
            return string.Format(ProblemFileNameFormat, numProblemId);
        }
    }
}