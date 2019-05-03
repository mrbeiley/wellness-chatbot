from bs4 import BeautifulSoup
import requests

url = "https://www.psychologytoday.com/us/therapists/az/chandler"
agent = {"User-Agent":'Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36'}

# Setup initial connection to psychologytoday

page = requests.get(url, headers=agent)
soup = BeautifulSoup(page.content, 'html.parser')

# Parse html for the therapists information

content = soup.find(class_="row results-content")
therapists_full = content.find_all(class_="row")
therapists_partial = [therapist.find(class_="result-row normal-result row") for therapist in therapists_full]
therapists_partial_clean = list(filter(None, therapists_partial))

therapist_names = [therapist["data-prof-name"] for therapist in therapists_partial_clean]
print(therapist_names)
