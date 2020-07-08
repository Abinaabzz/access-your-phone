using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using MySql.Data;
using MySql.Data.MySqlClient;
using System.Data;
public partial class dwnldfile : System.Web.UI.Page
{
    MySqlConnection con = new MySqlConnection("server=localhost;database=pconnect;username=root;password=''");

    protected void Page_Load(object sender, EventArgs e)
    {
        MySqlCommand cmd = new MySqlCommand();
        cmd.CommandText = "select * from filesup where upid='" + Session["id"] + "'";
        cmd.Connection = con;
        MySqlDataAdapter da = new MySqlDataAdapter();
        da.SelectCommand = cmd;
        DataSet ds = new DataSet();
        da.Fill(ds);
      DataGrid1.DataSource = ds.Tables[0];
      DataGrid1.DataBind();

    }
    protected void DataGrid1_ItemCommand(object source, DataGridCommandEventArgs e)
    {
        string filename = e.Item.Cells[1].Text;
        filename = Server.MapPath(filename);
        Response.ContentType = ContentType;
        Response.AppendHeader("content-Disposition", "attachment;filename=" + filename);
        Response.WriteFile(filename);

        Response.End();
    }
}