import os
import re

# Esta linea sirve para saber la rta en la que se encuentra nuestro script de python.
# ya que nos sera necesaria para conocer donde guardar los archivos de encriptacion 
# sin usar rutas absolutas. La idea es que se generen en el mismo directorio que el script
ruta_actual = os.path.dirname(os.path.abspath(__file__))

# 'palabras.txt' y 'cifradas.txt', siguiendo la ruta actual
ruta_palabras = os.path.join(ruta_actual, 'palabras.txt')
ruta_cifradas = os.path.join(ruta_actual, 'cifradas.txt')

def cifrar_fichero():
    # Usar la ruta completa del archivo 'palabras.txt' y 'cifradas.txt'. Se abren y se escriben/leen datos
    with open(ruta_palabras, 'r') as f_entrada, open(ruta_cifradas, 'w') as f_salida:
        for linea in f_entrada: #lee todas las lineas de palabras.txt
            texto = linea.strip()  # Elimina espacios y saltos de línea para asegurarnos de leer palabras.
            if not texto:
                continue  # Salta líneas vacías
            print(f"Palabra a cifrar: {texto}")
            texto = texto.upper() #convierte todo a mayusculas
            texto = re.sub(r'[^A-Za-z]', '', texto)  # Elimina los caracteres especiales, como pide el ejercicio

            # Pedir la clave del usuario, mostrandole la linea leida en 'palabras.txt'
            clave = input(f"Dame clave para cifrar (tiene que tener el mismo largo que '{texto}'): ").upper()
            #nos aseguramos de que la clave tiene el mismo largo que la palabra leida
            while len(clave) != len(texto):
                clave = input(f"La clave debe tener el mismo largo que '{texto}', intenta de nuevo: ").upper()

            # Cifrar el texto
            texto_cifrado = '' #primero se vacia por si contuviera algo
            for i in range(len(texto)):
                posicion_texto = ord(texto[i]) - ord('A') #coje posicion de la letra actual de la palabra
                posicion_clave = ord(clave[i]) - ord('A') #coje posicion de la letra actual de la clave
                posicion_cifrada = (posicion_texto + posicion_clave) % 26 # hace el resto para calcular que siempre sea un numero entre 1 y 26 sin incluir
                letra_cifrada = chr(posicion_cifrada + ord('A')) # otorga la posicion de la letra cifrada
                texto_cifrado += letra_cifrada #concatena la letra cifrada al contenido del texto cifrado

            f_salida.write(texto_cifrado + '\n') #escribe la palabra cifrada y un salto de linea

    print("Cifrado completado")

def descifrar_fichero():
    ruta_desencriptadas = os.path.join(ruta_actual, 'desencriptadas.txt')
    #abre el fichero de escritura y el de lectura
    with open(ruta_cifradas, 'r') as f_entrada, open(ruta_desencriptadas, 'w') as f_salida:
        for linea in f_entrada:
            #mismo proceso que el anterior: quita los espacios en blanco, caracteres epseciales y convierte todo a mayúsculas
            texto_cifrado = linea.strip()
            if not texto_cifrado:
                continue
            print(f"Palabra cifrada a descifrar: {texto_cifrado}")
            texto_cifrado = texto_cifrado.upper()
            #pide la clave y se asegura que tiene el mismo largo que la palabra cifrada
            clave = input(f"Dame clave para descifrar (tiene que tener el mismo largo que '{texto_cifrado}'): ").upper()

            while len(clave) != len(texto_cifrado):
                clave = input(f"La clave debe tener el mismo largo que '{texto_cifrado}', intenta de nuevo: ").upper()
            #mismo proceso para calcular la posicion de la nueva palabra a base de hacer lo contrario que en el anterior metodo
            texto_descifrado = ''
            for i in range(len(texto_cifrado)):
                pos_cifrada = ord(texto_cifrado[i]) - ord('A')
                pos_clave = ord(clave[i]) - ord('A')
                pos_texto = (pos_cifrada - pos_clave) % 26 #en vez de sumar, resta la posicion para sacar la palabra original
                letra_texto = chr(pos_texto + ord('A'))
                texto_descifrado += letra_texto

            f_salida.write(texto_descifrado + '\n') #la escribe en desencriptadas.txt

    print("Descifrado completado")
# Ejecutar cifrado y descifrado
cifrar_fichero()
descifrar_fichero()
