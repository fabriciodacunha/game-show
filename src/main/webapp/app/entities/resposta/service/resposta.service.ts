import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResposta, getRespostaIdentifier } from '../resposta.model';

export type EntityResponseType = HttpResponse<IResposta>;
export type EntityArrayResponseType = HttpResponse<IResposta[]>;

@Injectable({ providedIn: 'root' })
export class RespostaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/respostas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(resposta: IResposta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resposta);
    return this.http
      .post<IResposta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(resposta: IResposta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resposta);
    return this.http
      .put<IResposta>(`${this.resourceUrl}/${getRespostaIdentifier(resposta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(resposta: IResposta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resposta);
    return this.http
      .patch<IResposta>(`${this.resourceUrl}/${getRespostaIdentifier(resposta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IResposta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IResposta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRespostaToCollectionIfMissing(respostaCollection: IResposta[], ...respostasToCheck: (IResposta | null | undefined)[]): IResposta[] {
    const respostas: IResposta[] = respostasToCheck.filter(isPresent);
    if (respostas.length > 0) {
      const respostaCollectionIdentifiers = respostaCollection.map(respostaItem => getRespostaIdentifier(respostaItem)!);
      const respostasToAdd = respostas.filter(respostaItem => {
        const respostaIdentifier = getRespostaIdentifier(respostaItem);
        if (respostaIdentifier == null || respostaCollectionIdentifiers.includes(respostaIdentifier)) {
          return false;
        }
        respostaCollectionIdentifiers.push(respostaIdentifier);
        return true;
      });
      return [...respostasToAdd, ...respostaCollection];
    }
    return respostaCollection;
  }

  protected convertDateFromClient(resposta: IResposta): IResposta {
    return Object.assign({}, resposta, {
      respostaData: resposta.respostaData?.isValid() ? resposta.respostaData.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.respostaData = res.body.respostaData ? dayjs(res.body.respostaData) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((resposta: IResposta) => {
        resposta.respostaData = resposta.respostaData ? dayjs(resposta.respostaData) : undefined;
      });
    }
    return res;
  }
}
