using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Data;
using MySql.Data;
using MySql.Data.MySqlClient;
using System.IO;
using System.Drawing;

/// <summary>
/// Summary description for WebService
/// </summary>
[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
// [System.Web.Script.Services.ScriptService]
public class WebService : System.Web.Services.WebService {


    MySqlConnection con = new MySqlConnection("server=localhost;database=pconnect;username=root;password=''");
    MySqlCommand cmd = new MySqlCommand();
    string s = "";
    public WebService () {

        //Uncomment the following line if using designed components 
        //InitializeComponent(); 
    }

    [WebMethod]
    public string HelloWorld() {
        return "Hello World";
    }





    [WebMethod]
    public string UploadFile(string fbstr, string fileName)
    {
        // the byte array argument contains the content of the file  
        // the string argument contains the name and extension  
        // of the file passed in the byte array  

        byte[] f = Convert.FromBase64String(fbstr);


        try
        {
            // instance a memory stream and pass the  
            // byte array to its constructor  
            MemoryStream ms = new MemoryStream(f);
            // instance a filestream pointing to the  
            // storage folder, use the original file name  
            // to name the resulting file  
            FileStream fs = new FileStream(System.Web.Hosting.HostingEnvironment.MapPath
                        ("~/files/") + fileName, FileMode.Create);
            // write the memory stream containing the original  
            // file as a byte array to the filestream  
            ms.WriteTo(fs);
            // clean up  
            ms.Close();
            fs.Close();
            fs.Dispose();
            // return OK if we made it this far  
            return "OK";
        }
        catch (Exception ex)
        {
            // return the error message if the operation fails  
            return ex.Message.ToString();
        }
    }  



    [WebMethod]
    public string commandinsert(string cmndpass, string id, string phno)
    {


        cmd.CommandText = "SELECT * FROM `commandpassword` WHERE `phno`='" + phno + "'";
        cmd.Connection = con;
        MySqlDataAdapter da = new MySqlDataAdapter();
        da.SelectCommand = cmd;
        DataSet ds = new DataSet();
        da.Fill(ds);
        DataTable dt = ds.Tables[0];
        if (dt.Rows.Count > 0)
        {
            cmd.CommandText = "UPDATE `commandpassword` SET `cpassword`='" + cmndpass + "' WHERE `phno`='" + phno + "'";
            cmd.Connection = con;
            try
            {

                con.Open();
                cmd.ExecuteNonQuery();
                s = "success";

            }
            catch
            {
                s = "error";
            }
        }
        else
        {
            cmd.CommandText = "INSERT INTO `commandpassword` (`cpassword`,`userid`,`phno`) VALUES ('" + cmndpass + "','" + id + "','" + phno + "')";
            cmd.Connection = con;
            try
            {

                con.Open();
                cmd.ExecuteNonQuery();
                s = "success";

            }
            catch
            {
                s = "error";
            }
        }

        con.Close();
        return s;


    }
    [WebMethod]
    public string commandchange(string cmndpass, string id)
    {
        cmd.CommandText = "UPDATE `commandpassword` SET `cpassword`='"+cmndpass+"' WHERE `userid`='"+id+"'";
        cmd.Connection = con;
        try
        {

            con.Open();
            cmd.ExecuteNonQuery();
            s = "success";

        }
        catch
        {
            s = "error";
        }
        con.Close();
        return s;


    }
    [WebMethod]
    public string commadcheck(string cmndpass, string id)
    {
        cmd.CommandText = "SELECT * FROM `commandpassword` WHERE `cpassword`='" + cmndpass + "' AND `userid`='" + id + "'";
        cmd.Connection = con;
        MySqlDataAdapter da = new MySqlDataAdapter();
        da.SelectCommand = cmd;
        DataSet ds = new DataSet();
        da.Fill(ds);
        DataTable dt = ds.Tables[0];
        if (dt.Rows.Count > 0)
        {
            s = "success";
        }
        else
        {
            s = "error";
        }
        con.Close();
        return s;


    }
    [WebMethod]
    public string commadsetcheck(string phno)
    {
        cmd.CommandText = "SELECT * FROM `commandpassword` WHERE `phno`='" + phno + "'";
        cmd.Connection = con;
        MySqlDataAdapter da = new MySqlDataAdapter();
        da.SelectCommand = cmd;
        DataSet ds = new DataSet();
        da.Fill(ds);
        DataTable dt = ds.Tables[0];
        if (dt.Rows.Count > 0)
        {
            s = "success";
        }
        else
        {
            s = "error";
        }
        con.Close();
        return s;


    }




    [WebMethod]
    public string take_cmndpass(string phno)
    {
        cmd.CommandText = "SELECT * FROM `commandpassword` WHERE  `phno`='" + phno + "'";
        cmd.Connection = con;
        MySqlDataAdapter da = new MySqlDataAdapter();
        da.SelectCommand = cmd;
        DataSet ds = new DataSet();
        da.Fill(ds);
        DataTable dt = ds.Tables[0];
        if (dt.Rows.Count > 0)
        {
            s =dt.Rows[0][1].ToString();
        }
        else
        {
            s = "error";
        }
        con.Close();
        return s;


    }




    //[WebMethod]
    //public string commadsetcheck(string id, string cmndpass)
    //{
    //    cmd.CommandText = "SELECT * FROM `commandpassword` WHERE `userid`='" + id + "'";
    //    cmd.Connection = con;
    //    MySqlDataAdapter da = new MySqlDataAdapter();
    //    da.SelectCommand = cmd;
    //    DataSet ds = new DataSet();
    //    da.Fill(ds);
    //    DataTable dt = ds.Tables[0];
    //    if (dt.Rows.Count > 0)
    //    {
    //        cmd.CommandText = "UPDATE `commandpassword` SET `cpassword`='" + cmndpass + "' WHERE `userid`='" + id + "'";
    //        cmd.Connection = con;
    //        try
    //        {

    //            con.Open();
    //            cmd.ExecuteNonQuery();
    //            s = "success";

    //        }
    //        catch
    //        {
    //            s = "error";
    //        }

           
    //    }
    //    else
    //    {
    //        cmd.CommandText = "INSERT INTO `commandpassword` (`cpassword`,`userid`) VALUES ('" + cmndpass + "','" + id + "')";
    //        cmd.Connection = con;
    //        try
    //        {

    //            con.Open();
    //            cmd.ExecuteNonQuery();
    //            s = "success";

    //        }
    //        catch
    //        {
    //            s = "error";
    //        }
    //        con.Close();
    //        return s;
    //    }
    //    con.Close();
    //    return s;


    //}

    [WebMethod]
    public void up_file_to_insert(String fnm, String phno, string cpass)
    {
        cmd.CommandText = "SELECT * FROM `commandpassword` WHERE  `phno`='" + phno + "' and cpassword='"+cpass+"'";
        cmd.Connection = con;
        MySqlDataAdapter da = new MySqlDataAdapter();
        da.SelectCommand = cmd;
        DataSet ds = new DataSet();
        da.Fill(ds);
        DataTable dt = ds.Tables[0];
        string p = "~/files/" + fnm;
        if (dt.Rows.Count > 0)
        {
            s = dt.Rows[0][0].ToString();
            cmd.CommandText = "INSERT INTO `filesup` (`upid`,`flnm`) VALUES ('" + s + "','" + p + "')";
            cmd.Connection = con;
            try
            {

                con.Open();
                cmd.ExecuteNonQuery();
                s = "success";

            }
            catch
            {
                s = "error";
            }
        }
        else
        {
            s = "error";
        }
        con.Close();

    }
    
}
