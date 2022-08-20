#!/usr/bin/sh

openssl rand -base64 768 > shared.key
chmod 400 shared.key