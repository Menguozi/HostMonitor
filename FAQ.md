## 1. JschSSHManager连接openEuler 22.03失败

使用JschSSHManager连接openEuler 22.03失败，通常是由OpenSSH 8.8版本引入的安全策略变更导致的算法协商问题。openEuler 22.03默认搭载OpenSSH 8.8，该版本出于安全考虑，默认禁用了ssh-rsa公钥签名算法。

### 常见原因分析：

这是最常见的报错，表现为Algorithm negotiation fail或no matching host key type found

现象：Java程序使用JSch库连接时抛出异常，提示无法协商算法。

原因：OpenSSH 8.8认为传统的ssh-rsa(RSA SHA-1)签名算法不够安全，因此默认不再接受此类密钥。如果你的服务器仍在使用ssh-rsa密钥，而客户端（JSch）没有显式声明支持，连接就会失败。

### 可行解决方案：

#### 方案一：修改服务端配置（推荐）

通过修改openEuler服务器的SSH配置文件，重新允许ssh-rsa算法。

(1) 编辑配置文件：使用root权限编辑/etc/ssh/sshd_config文件。

    vi /etc/ssh/sshd_config

(2) 添加或修改配置：在文件末尾添加以下行，或者找到对应行取消注释并修改。

    HostKeyAlgorithms +ssh-rsa          #允许客户端接受服务器提供的ssh-rsa主机密钥
    PubkeyAcceptedAlgorithms +ssh-rsa   #允许服务器接受客户端使用的ssh-rsa公钥进行认证

(3) 重启SSH服务：保存文件后，重启SSH服务使配置生效。

    systemctl restart sshd

#### 方案二：修改客户端代码（JSch 配置）

如果您无法修改服务器配置，可以在Java代码中显式指定支持的算法。

在创建 JSch 会话之前，添加以下配置：

    // 允许使用 ssh-rsa 算法
    Properties config = new Properties();
    config.put("server_host_key", "ssh-rsa");
    config.put("PubkeyAcceptedKeyTypes", "ssh-rsa");
    session.setConfig(config);