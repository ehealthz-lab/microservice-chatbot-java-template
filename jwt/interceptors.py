import sys
import json
from JWT import jwt_validator


with open('security.conf') as config_file:
   config = json.loads(config_file.read())

validator = jwt_validator(config['jwt_validator'])

authHeader = sys.argv[1]

result = "False"
aux = authHeader.split(' ')
if aux[0] == 'Bearer':
	token = aux[1]
	decoded_token = validator.decode_token(token)
	if decoded_token != None:
		print decoded_token['sub']

	