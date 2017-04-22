
import shutil
import os
import shlex
import sys
import subprocess
from subprocess import PIPE
import xml.etree.ElementTree as ET

"""
tree = ET.parse('/home/oran/repos/code-duplication/projects/Traceur_before.xml')
# tree = ET.fromstring(out)
root = tree.getroot()
for child in root:
	print child.tag, child.attrib

exit()
"""

if len(sys.argv) != 5:
	print "Usage: {0} PMD_SH_PATH SPARTANAIZER_JAR_PATH PROJECTS_DIR AFTER_SPARTANAIZE_PROJECTS_DIR".format(__file__)
	exit(-1)

MINIMUM_TOKENS = 10
PMD_SH_PATH = sys.argv[1]
SPARTANAIZER_JAR_PATH = sys.argv[2]
BEFORE_SPART = sys.argv[3]
CLONES_OUTPUT = os.path.join(BEFORE_SPART, '..')
AFTER_SPART = sys.argv[4]
HEADERS = ['project', 'before', 'after', 'percentage']
COLUMNS_WIDTH = [30, 10, 10, 10]
DELIM_ROW = ['-' * w for w in COLUMNS_WIDTH]

def run_proc(cmd):
	proc = subprocess.Popen(shlex.split(cmd), stdout=subprocess.PIPE, stdin=subprocess.PIPE, stderr=subprocess.PIPE)
	out, err = proc.communicate()
	return out, err

def get_num_duplicates(dirpath, clones_output_file_name):
	out, err = run_proc("{pmd_sh_path} cpd --minimum-tokens {minimum_tokens} --language java --format xml --files {dirpath}".format(pmd_sh_path=PMD_SH_PATH,minimum_tokens=MINIMUM_TOKENS, dirpath=dirpath))
	file_path = os.path.join(CLONES_OUTPUT, clones_output_file_name)
	with open(file_path, 'wb') as f:
		f.write(out)
	return out.count('<duplication')

def analyze_project(project_name, project_num):	
	before_spart_path = os.path.join(BEFORE_SPART, project_name)
	after_spart_path = os.path.join(AFTER_SPART, project_name)
	
	print str(project_num) + ") " + project_name + ":"
	print "num duplicates before = ",
	num_duplicates_before = get_num_duplicates(before_spart_path, project_name+'_before.xml')
	print num_duplicates_before
	print "Spartinizing project... ",
	if not os.path.exists(after_spart_path):
		shutil.copytree(before_spart_path, after_spart_path)
		cmd = 'timeout 120 java -jar {spartanaizer_jar_path} {dirpath}'.format(spartanaizer_jar_path=SPARTANAIZER_JAR_PATH,dirpath=after_spart_path)
		out, err = run_proc(cmd)
		if err:
			shutil.rmtree(after_spart_path)
			print err
			exit(-1)
	
	print "Done!"
	print "num duplicates after = ",
	
	num_duplicates_after = get_num_duplicates(after_spart_path, project_name+'_after.xml')
	print num_duplicates_after
	print ''

	return (project_name, num_duplicates_before, num_duplicates_after)

def build_row(*values):
	return "|".join([str(values[i]).ljust(COLUMNS_WIDTH[i]) for i in range(len(values))])

def calc_percentage(before, after):
	if float(r[1]) == 0:
		return 0.0
	return int((r[2] - r[1]) / float(r[1]) * 100.0)

res = []
num = 1
for dirname in os.listdir(BEFORE_SPART):
    if os.path.isdir(os.path.join(BEFORE_SPART, dirname)):
    	res.append(analyze_project(dirname, num))
    	num += 1

print '================================ Final results ================================'
print build_row(*HEADERS)
print build_row(*DELIM_ROW)
for r in res:
	perc = '{0}%'.format(calc_percentage(r[1], r[2]))
	print build_row(r[0], r[1], r[2], perc)
print build_row(*DELIM_ROW)
