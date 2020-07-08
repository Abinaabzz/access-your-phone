using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using MySql.Data;
using MySql.Data.MySqlClient;
using System.Data;
public partial class login : System.Web.UI.Page
{
    MySqlConnection con = new MySqlConnection("server=localhost;database=pconnect;username=root;password=''");
    protected void Page_Load(object sender, EventArgs e)
    {

    }
    protected void Button1_Click(object sender, EventArgs e)
    {
        MySqlCommand cmd = new MySqlCommand();
        cmd.CommandText = "select * from commandpassword where phno='"+TextBox1.Text+"'";
        cmd.Connection = con;
        MySqlDataAdapter da = new MySqlDataAdapter();
        da.SelectCommand = cmd;
        DataSet ds = new DataSet();
        da.Fill(ds);
        DataTable dt = ds.Tables[0];
        if (dt.Rows.Count > 0)
        {
            Session["id"] = dt.Rows[0][0];
            Response.Redirect("dwnldfile.aspx");
        }
        else
        {
            Response.Write("<script>alert('Type valid Number')</script>");
                
        }
    }
}