given the following structure in project root under test folder:

```
test
|-- com
    `-- oracle
        `-- git
            `-- cx_ws
                `-- it_cx_soap.clj
```

you can test this with: 

```bash
$ lein test com.oracle.git.cx_ws.it_cx_soap
```

Notice:

* don't specify the test folder
* don't use `/` (forward slashes)
* don't include final: `*.clj`
