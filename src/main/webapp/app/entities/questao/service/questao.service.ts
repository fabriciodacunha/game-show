import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestao, getQuestaoIdentifier } from '../questao.model';

export type EntityResponseType = HttpResponse<IQuestao>;
export type EntityArrayResponseType = HttpResponse<IQuestao[]>;

@Injectable({ providedIn: 'root' })
export class QuestaoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/questaos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questao: IQuestao): Observable<EntityResponseType> {
    return this.http.post<IQuestao>(this.resourceUrl, questao, { observe: 'response' });
  }

  update(questao: IQuestao): Observable<EntityResponseType> {
    return this.http.put<IQuestao>(`${this.resourceUrl}/${getQuestaoIdentifier(questao) as number}`, questao, { observe: 'response' });
  }

  partialUpdate(questao: IQuestao): Observable<EntityResponseType> {
    return this.http.patch<IQuestao>(`${this.resourceUrl}/${getQuestaoIdentifier(questao) as number}`, questao, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuestaoToCollectionIfMissing(questaoCollection: IQuestao[], ...questaosToCheck: (IQuestao | null | undefined)[]): IQuestao[] {
    const questaos: IQuestao[] = questaosToCheck.filter(isPresent);
    if (questaos.length > 0) {
      const questaoCollectionIdentifiers = questaoCollection.map(questaoItem => getQuestaoIdentifier(questaoItem)!);
      const questaosToAdd = questaos.filter(questaoItem => {
        const questaoIdentifier = getQuestaoIdentifier(questaoItem);
        if (questaoIdentifier == null || questaoCollectionIdentifiers.includes(questaoIdentifier)) {
          return false;
        }
        questaoCollectionIdentifiers.push(questaoIdentifier);
        return true;
      });
      return [...questaosToAdd, ...questaoCollection];
    }
    return questaoCollection;
  }
}
