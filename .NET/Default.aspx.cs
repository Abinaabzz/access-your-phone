using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class _Default : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        string vTitle = "";
        string vDesc = "";
        

        if (!string.IsNullOrEmpty(Request.Form["title"]))
        {
            vTitle = Request.Form["title"];
        }
        if (!string.IsNullOrEmpty(Request.Form["description"]))
        {
            vDesc = Request.Form["description"];
        }

        //vTitle = "aa.jpg";
        string FilePath = Server.MapPath("~/files/" + vTitle);

        HttpFileCollection MyFileCollection = Request.Files;
        if (MyFileCollection.Count > 0)
        {
            // Save the File
            MyFileCollection[0].SaveAs(FilePath);
        }
        //Response.Write(FilePath);
    }
}