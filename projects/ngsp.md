# NGSP

Next Generation Support Platform, application requirements

## Applications

There are five (5) custom applications that need to be deployed.  They
are:

* CRMOD-Secure Enterprise Search Tab

* TAS Integration

* Mass Update of Master Incident

* HRMS to CRMOD user data integration

* AutoSR Replacement

## Deployment Format

All applications are packaged as EAR (Enterprise Archive) files and
need the same infrastructure for deployment.

## Environmental Requirements

Currently these applications are deployed to the following
environment.

* Oracle Enterprise Linux 5 Update 5
* Glassfish V3

In production we can replace Glassfish with the latest version of WebLogic

## Application Requirements Breakdown

### CRMOD - Secure Enterprise Search

* [Design Document](http://linux1.hk.oracle.com/MvnSesFe/design.html)
* [Deployment Guide](http://linux1.hk.oracle.com/SES-CRMOD-EAR/)
* Response Payload Size: 12.4 KB

### TAS Integration

* [Design Document] (http://linux1.hk.oracle.com/crmodTasItgrtn/design.html)
* [Deployment Guide] (http://linux1.hk.oracle.com/crmodTasItgrtn/)

### Mass Update of Master Incident

* Design Document
* Deployment Guide

### HRMS to CRMOD user data integration

* Design Document
* Deployment Guide

### AutoSR Replacement (AutoSR2)

* [Design Document](http://linux1.hk.oracle.com/autoSR2/design.html)
* [Deployment Guide](http://linux1.hk.oracle.com/autoSR2-EAR/)

All five (5) applications can be hosted on a single server with the
following specifications:

* Disk: 100GB
* CPU: Dual Core 2 GHz
* RAM: 8GB
