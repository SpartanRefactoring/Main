import urllib2
import re
import os
import sys

if len(sys.argv) != 2:
	print "Usage: {0} BASE_DIR".format(__file__)
	exit(-1)
	
BASE_DIR = sys.argv[1]

excluded_projects = []

response = urllib2.urlopen("https://github.com/trending/java")
page_source = response.read()

srcs = re.findall(r'<div class="d-inline-block col-9 mb-1">\s*<h3>\s*<a href="(.*)">[\s\S]*?<\/a>\s*<\/h3>\s*<\/div>', page_source)
srcs = filter(lambda x: x.lower() not in [s.lower() for s in excluded_projects], srcs)
repos_urls = ['https://github.com{0}.git'.format(s) for s in srcs]

os.chdir(BASE_DIR)

for url in repos_urls[:25]:
	dir_name = url[url.rfind('/') + 1:url.rfind('.')]

	if os.path.exists(dir_name):
		print dir_name + ' already exists... ({0})'.format(url)
	else:
		print 'Cloning {0}'.format(url)
		os.system('git clone %s' % url)
		
		
		
