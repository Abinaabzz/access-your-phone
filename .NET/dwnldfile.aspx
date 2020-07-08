<%@ Page Language="C#" AutoEventWireup="true" CodeFile="dwnldfile.aspx.cs" Inherits="dwnldfile" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <style type="text/css">
        .style1
        {
            width: 100%;
        }
    </style>
</head>
<body>
    <form id="form1" runat="server">
    <table class="style1">
        <tr>
            <td>
                &nbsp;</td>
            <td>
                <asp:DataGrid ID="DataGrid1" runat="server" AutoGenerateColumns="False" 
                    onitemcommand="DataGrid1_ItemCommand">
                    <Columns>
                        <asp:BoundColumn DataField="id" HeaderText="Slno"></asp:BoundColumn>
                        <asp:BoundColumn DataField="flnm" HeaderText="files"></asp:BoundColumn>
                        <asp:ButtonColumn Text="Download"></asp:ButtonColumn>
                    </Columns>
                </asp:DataGrid>
            </td>
        </tr>
        <tr>
            <td>
                &nbsp;</td>
            <td>
                &nbsp;</td>
        </tr>
    </table>
    <div>
    
    </div>
    </form>
</body>
</html>
