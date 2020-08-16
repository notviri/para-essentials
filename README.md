# para server plugin

Setup with no permissions plugin:
```yml
# put this in your 'permissions.yml'
server.basics:
  description: Basic permissions.
  default: true
  children:
    bukkit.command.tps: true
    para.command.help: true
    para.command.ignore: true
    para.command.ignorelist: true
    para.command.msg: true
    para.command.r: true
    para.command.togglechat: true
    para.command.toggledeathmsgs: true
    para.command.kill: true
```
